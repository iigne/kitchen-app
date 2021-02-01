import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faSearch, faPlus
} from "@fortawesome/free-solid-svg-icons";

import './UserStock.css';
import {Row, Col, Button, Container, Form} from "react-bootstrap";
import axios from "axios";
import authHeader from "../../api/auth-header";
import Search from "./Search";


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
        // this.handleSubmit = this.handleSubmit.bind(this)
        // this.handleChange = this.handleChange.bind(this)
        // this.handleSearch = this.handleSearch.bind(this)
        // this.handleSearchSelection = this.handleSearchSelection.bind(this)
    }

    handleSearchSelection = (id) => {
        const selected = {...this.state.searchResults[id]}
        this.setState({selectedIngredient: selected})
        this.setState({searchResults: []})
        this.setState({noResults: null})
        this.setState({searchTerm: selected.name})
        this.setState({selectedMeasurement: selected.measurements[0].id})
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
        this.setState({[e.target.name]: e.target.value}, () => {
            console.log("after measurement change", this.state)
        })
    }

    handleSubmit = () => {
        let ingredient = {...this.state.selectedIngredient}
        let quantity = {
            quantity: this.state.quantity,
            measurementId: this.state.selectedMeasurement
        }
        const body = {
            ingredient: {
                id: ingredient.id
            },
            quantities: [quantity]
        }
        console.log(body)
        axios.post('/user-ingredient', {
            ingredient: {
                id: ingredient.id
            },
            quantities: [quantity]}, {
            headers: authHeader()
        }).then(res => {
            this.props.addIngredientHandler(res.data)
        }).catch(error => {
            console.log(error)
        });
    }

    render() {
        let isIngredientSelected = this.state.selectedIngredient != null
        if(isIngredientSelected) {

        }
        return (
            <Container className="ingredient">
                <Form.Group controlId="formAddIngredient">
                    <Row>
                        <Col xs={1}><i><FontAwesomeIcon icon={faSearch}/></i></Col>
                        <Col xs={6}><Form.Control name="searchTerm" placeholder="search for ingredient"
                                                  onChange={this.handleSearch}/></Col>
                        {this.state.selectedIngredient != null ?
                            <>
                                <Col><Form.Control name="quantity" type="number" placeholder="100"
                                                   onChange={this.handleChange}/></Col>
                                <Col>
                                    <Form.Control as="select" name="selectedMeasurement" onChange={this.handleChange}>
                                        {this.state.selectedIngredient.measurements.map((item, index) =>
                                            <option value={item.id}>{item.name}</option>)}
                                    </Form.Control>
                                </Col>
                                <Col xs={1} className="float-right">
                                    <Button name="add" variant="outline-success"
                                            onClick={this.handleSubmit}><FontAwesomeIcon
                                        icon={faPlus}/></Button>
                                </Col>
                            </> : <div/>
                        }

                    </Row>
                    {/*TODO move below into own component*/}
                    {this.state.searchResults.map((item, id) =>
                        <Row>
                            <Col xs={1}/>
                            <Col xs={6}>
                                <Button onClick={() => this.handleSearchSelection(id)} variant="outline-light"
                                        block>{item.name}</Button>
                            </Col>
                        </Row>
                    )}
                    {this.state.noResults === true ?
                        <div>The ingredient you're searching for has not been found. Would you like to create
                            it?</div> : ""}
                </Form.Group>
            </Container>
        )
    }

}

export default AddIngredient;