import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faSearch, faPlus
} from "@fortawesome/free-solid-svg-icons";

import './UserStock.css';
import {Row, Col, Button, Container, Form} from "react-bootstrap";


class AddIngredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: "",
            quantity: 1,
            measurement: "grams",
            category: "Vegetable"
        }
        this.handleSubmit = this.handleSubmit.bind(this)
        this.handleChange = this.handleChange.bind(this)

    }

    handleChange(e) {
        this.setState({[e.target.name]: e.target.value})
    }

    handleSubmit() {
        let ingredient = {...this.state}
        this.props.addIngredientHandler(ingredient)
    }

    render() {
        return (
                <Container className="ingredient">
                    <Form.Group controlId="formAddIngredient">
                        <Row>
                            <Col xs={1}><i><FontAwesomeIcon icon={faSearch}/></i></Col>
                            <Col xs={6}><Form.Control name="name" placeholder="ingredient name" onChange={this.handleChange}/></Col>
                            <Col><Form.Control name="quantity" type="number" placeholder="100" onChange={this.handleChange}/></Col>
                            <Col>
                                <Form.Control as="select" name="measurement" onChange={this.handleChange}>
                                    <option>grams</option>
                                </Form.Control>
                            </Col>
                            <Col xs={1} className="float-right">
                                    <Button name="add" variant="outline-success" onClick={this.handleSubmit}><FontAwesomeIcon
                                        icon={faPlus}/></Button>
                            </Col>
                        </Row>
                    </Form.Group>
                </Container>
        )
    }

}

export default AddIngredient;