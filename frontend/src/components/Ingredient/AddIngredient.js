import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus, faSearch} from "@fortawesome/free-solid-svg-icons";

import '../UserStock/UserStock.css';
import {Button, Col, Container, Form, FormGroup, FormLabel, InputGroup, ListGroup, Row} from "react-bootstrap";
import axios from "axios";
import authHeader from "../../api/auth-header";
import CreateIngredient from "./CreateIngredient";


class AddIngredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            searchTerm: "",
            searchResults: [],
            noResults: null,
            selectedIngredient: null,
            quantity: 100,
            selectedMeasurement: null,
            inCreateIngredient: false
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
                this.props.showAlert("Search has failed", "error")
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
        let ingredient = {...this.state.selectedIngredient};
        const measurementId = parseInt(this.state.selectedMeasurement);
        const index = ingredient.measurements.findIndex(m => m.id === measurementId);
        const selectedMeasurement = ingredient.measurements[index];
        this.props.addIngredientHandler({
            id: ingredient.id,
            name: ingredient.name,
            measurementId: measurementId,
            measurementName: selectedMeasurement.name,
            category: ingredient.category,
            quantity: this.state.quantity,
        })
        this.setState({
            searchTerm: "",
            searchResults: [],
            noResults: null,
            selectedIngredient: null,
            selectedMeasurement: null
        })
    }

    enterEnterCreateIngredient = () => {
        this.setState({inCreateIngredient:true})
        this.setState({searchResults: []})
        this.setState({noResults: null})
    }

    handleFinishCreateIngredient = (success, name, status) => {
        if(success) {
            this.setState({inCreateIngredient: false})
            this.setState({searchTerm: name})
            this.props.showAlert("Ingredient created - you can now search for it", "success");
        } else {
            console.log(status);
            if(status === 400) {
                this.props.showAlert("Creation failed - some fields have invalid values", "error");
            } else {
                this.props.showAlert("Failed to create ingredient", "error");
            }
        }
    }

    cancelCreate = () => {
        this.setState({inCreateIngredient: false})
    }

    render() {
        let inCreateIngredient = this.state.inCreateIngredient
        return (
            <Container>
                <Form>
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
                            {this.state.noResults === true ?
                                <Row>
                                    <Col>
                                        <ListGroup.Item className="notFoundItem">The ingredient you're searching for has
                                            not been found. Would you like to create it?

                                            <Button variant="success" onClick={this.enterEnterCreateIngredient} size="sm"><FontAwesomeIcon
                                                icon={faPlus}/></Button>
                                        </ListGroup.Item>
                                    </Col>
                                </Row>

                                : ""}
                        </ListGroup>
                    </FormGroup>


                    {/*TODO move below into own component*/}


                    {this.state.selectedIngredient != null ?
                        <FormGroup>
                            <Row>
                                <Col>
                                    <FormLabel>Quantity</FormLabel>
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={4}>
                                    <Form.Control name="quantity" type="number" placeholder="100"
                                                  onChange={this.handleChange}/></Col>
                                <Col xs={3}>
                                    <Form.Control as="select" name="selectedMeasurement" onChange={this.handleChange}>
                                        {this.state.selectedIngredient.measurements.map((item) =>
                                            <option key={item.id} value={item.id}>{item.name}</option>)}
                                    </Form.Control>
                                </Col>
                                <Col>
                                    <Button className="float-right" name="add" variant="outline-success"
                                            onClick={this.handleAddIngredient}><FontAwesomeIcon
                                        icon={faPlus}/></Button>
                                </Col>
                            </Row>
                        </FormGroup>
                        : null
                    }

                </Form>
                <CreateIngredient show={inCreateIngredient}
                                  handleFinishCreateIngredient={this.handleFinishCreateIngredient}
                                  cancelCreate={this.cancelCreate}
                />

            </Container>


        )
    }

}

export default AddIngredient;