import {Button, Col, Form, FormGroup, FormLabel, InputGroup, ListGroup, Row} from "react-bootstrap";
import React from "react";
import axios from "axios";
import authHeader from "../../api/auth-header";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus, faSearch} from "@fortawesome/free-solid-svg-icons";

class Search extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchTerm: "",
            searchResults: [],
            noResults: null,
            typingTimeout: 0
        }
    }


    handleSearch = (e) => {
        const searchText = e.target.value

        this.props.handleSearch();
        this.resetSearch();
        this.setState({searchTerm: searchText})
        // setting a timeout to wait until user finishes typing
        // so that BE is not constantly bombarded with requests
        if (this.state.typingTimeout) {
            clearTimeout(this.state.typingTimeout);
        }
        this.setState({
            typingTimeout:
                setTimeout(() => {
                    this.search(searchText);
                }, 900)
        })

    }

    search = (searchText) => {
        if (searchText !== "") {
            axios.get('/ingredient/search', {
                params: {term: searchText},
                headers: authHeader()
            }).then(
                res => {
                    this.setState({
                        searchResults: res.data,
                        noResults: res.data.length === 0
                    })
                }
            ).catch(error => {
                this.props.showAlert("Search has failed", "error")
                console.log(error)
            })
        }
    }

    handleSearchSelection = (id) => {
        this.setState(prevState => {
            const searchResults = prevState.searchResults
            const index = searchResults.findIndex(sr => sr.id === id)
            const selected = searchResults[index]
            this.props.handleSearchSelection(selected)
            return {
                searchResults: [],
                noResults: null,
                searchTerm: "",
            }
        })
    }

    enterCreateIngredient = () => {
        this.resetSearch();
        this.props.enterCreateIngredient();
    }

    resetSearch = () => {
        this.setState({searchResults: []})
        this.setState({noResults: null})
    }

    render() {
        return (
            <FormGroup>
                <Row>
                    <Col>
                        <FormLabel>Search for ingredient</FormLabel>
                        <InputGroup>
                            <InputGroup.Prepend>
                                <span className="input-group-text"> <FontAwesomeIcon icon={faSearch}/></span>
                            </InputGroup.Prepend>

                            <Form.Control value={this.state.searchTerm} name="searchTerm"
                                          onChange={this.handleSearch}/>
                        </InputGroup>
                    </Col>
                </Row>

                <ListGroup>
                    {this.state.searchResults.map((item) =>
                        <Row key={item.id}>
                            <Col>
                                <ListGroup.Item action className="searchListItem"
                                                onClick={() => this.handleSearchSelection(item.id)}>
                                    {item.name}
                                </ListGroup.Item>
                            </Col>
                        </Row>
                    )}
                    {this.state.noResults &&
                    <Row>
                        <Col>
                            <ListGroup.Item className="notFoundItem">The ingredient you're searching for has
                                not been found. Would you like to create it?

                                <Button variant="success" onClick={this.enterCreateIngredient}
                                        size="sm"><FontAwesomeIcon
                                    icon={faPlus}/></Button>
                            </ListGroup.Item>
                        </Col>
                    </Row>
                    }
                </ListGroup>
            </FormGroup>
        )

    }
}

export default Search;