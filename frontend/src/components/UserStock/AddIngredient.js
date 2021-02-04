import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus, faSearch} from "@fortawesome/free-solid-svg-icons";

import './UserStock.css';
import {Button, Col, Container, Form, ListGroup, Row} from "react-bootstrap";
import axios from "axios";
import authHeader from "../../api/auth-header";


class AddIngredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            searchTerm: "",
            searchResults: [],
            noResults: null,
            selectedIngredient: null,
            quantity: 1,
            selectedMeasurement: null,
        }
    }

    handleSearchSelection = (id) => {
        this.setState(prevState => {
            const searchResults = prevState.searchResults
            const index = searchResults.findIndex(sr => sr.id === id)
            const selected = searchResults[index]
            return {
                selectedIngredient: selected,
                searchResults: [],
                noResults: null,
                searchTerm: selected.name,
                selectedMeasurement: selected.measurements[0].id
            }
        })
    }

    handleSearch = (e) => {
        this.setState({selectedIngredient: null})
        this.setState({selectedMeasurement: null})
        this.setState({[e.target.name]: e.target.value})
        if (e.target.value !== "") {
            axios.get('/ingredient/search', {
                params: {term: e.target.value},
                headers: authHeader()
            }).then(
                res => {
                    this.setState({searchResults: res.data})
                    if (this.state.searchResults.length === 0) {
                        this.setState({noResults: true})
                    } else {
                        this.setState({noResults: false})
                    }
                }
            ).catch(error => {
                console.log(error)
            })
        } else {
            this.setState({searchResults: []})
            this.setState({noResults: null})
        }
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value})
    }

    handleAddIngredient = () => {
        let ingredient = {...this.state.selectedIngredient}
        let quantity = {
            quantity: this.state.quantity,
            measurementId: this.state.selectedMeasurement
        }
        axios.post('/user-ingredient', {
            ingredient: {
                id: ingredient.id
            },
            quantities: [quantity]
        }, {
            headers: authHeader()
        }).then(res => {
            this.props.addIngredientHandler(res.data)
            this.setState({searchTerm: ""})
            this.setState({searchResults: []})
            this.setState({noResults: null})
            this.setState({selectedIngredient: null})
            this.setState({selectedMeasurement: null})
        }).catch(error => {
            console.log(error)
        });
    }

    render() {
        return (
            <Container className="ingredient">
                <Form.Group controlId="formAddIngredient">
                    <Row>
                        <Col xs={1}><i><FontAwesomeIcon icon={faSearch}/></i></Col>
                        <Col xs={6}><Form.Control value={this.state.searchTerm} name="searchTerm"
                                                  placeholder="search for ingredient"
                                                  onChange={this.handleSearch}/></Col>
                        {this.state.selectedIngredient != null ?
                            <>
                                <Col xs={2}><Form.Control name="quantity" type="number" placeholder="100"
                                                          onChange={this.handleChange}/></Col>
                                <Col xs={2}>
                                    <Form.Control as="select" name="selectedMeasurement" onChange={this.handleChange}>
                                        {this.state.selectedIngredient.measurements.map((item) =>
                                            <option key={item.id} value={item.id}>{item.name}</option>)}
                                    </Form.Control>
                                </Col>
                                <Col xs={1} className="float-right">
                                    <Button name="add" variant="outline-success"
                                            onClick={this.handleAddIngredient}><FontAwesomeIcon
                                        icon={faPlus}/></Button>
                                </Col>
                            </> : <div/>
                        }

                    </Row>

                    {/*TODO move below into own component*/}
                    <Row>
                        <Col xs={1}/>
                        <Col xs={6}>
                            <ListGroup>
                                {this.state.searchResults.map((item) =>
                                    <ListGroup.Item key={item.id} action className="searchListItem"
                                                    onClick={() => this.handleSearchSelection(item.id)}
                                    >{item.name}</ListGroup.Item>
                                )}
                                {this.state.noResults === true ?
                                    <ListGroup.Item className="notFoundItem">The ingredient you're searching for has not
                                        been found.
                                        Would you like to create it?

                                        <Button variant="success" size="sm"><FontAwesomeIcon
                                            icon={faPlus}/></Button>
                                        <span className="small-text">(Feature not implemented yet)</span>
                                    </ListGroup.Item>
                                    : ""}
                            </ListGroup>
                        </Col>
                    </Row>

                </Form.Group>


            </Container>


        )
    }

}

export default AddIngredient;