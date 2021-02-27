import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faAppleAlt,
    faBox,
    faBreadSlice,
    faCheck,
    faLeaf,
    faPepperHot,
    faQuestionCircle,
    faSnowflake
} from "@fortawesome/free-solid-svg-icons";

import axios from "axios";


import './UserStock.css';
import {Button, Col, Dropdown, Form, Row, ListGroup} from "react-bootstrap";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownItem from "react-bootstrap/DropdownItem";


class Ingredient extends React.Component {

    constructor(props) {
        console.log(props);
        super(props);
        this.state = {
            id: props.id,
            name: props.name,
            quantity: props.quantity,
            measurement: props.measurementId,
            measurementName:props.measurementName,
            category: this.getIcon(props.category),

            currentlyEditing: false,
            editedQuantity: null
        }
    }

    getIcon(category) {
        switch (category) {
            case "Vegetable":
                return faLeaf;
            case "Fruit":
                return faAppleAlt;
            case "Bread":
                return faBreadSlice;
            case "Cupboard":
                return faBox;
            case "Fridge":
                return faSnowflake;
            case "Freezer":
                return faSnowflake;
            case "Spice":
                return faPepperHot;
            default:
                return faQuestionCircle;
        }
    }

    deleteIngredient = () => {
        this.props.removeIngredientHandler(this.state.id);
    }

    startEditIngredient = () => {
        this.setState({editedQuantity: this.state.quantity})
        this.setState({currentlyEditing: true})
    }

    updateIngredient = () => {
        const newQuantity = this.state.editedQuantity;
        const ingredientId = this.state.id;
        const measurementId = this.state.measurement;

        this.props.updateIngredientHandler({
            newQuantity: newQuantity,
            ingredientId: ingredientId,
            measurementId: measurementId
        });

        this.setState({quantity: newQuantity})
        this.setState({currentlyEditing: false})
        this.setState({editedQuantity: null})
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value})
    }

    render() {
        const currentlyEditing = this.state.currentlyEditing

        return (
            <ListGroup.Item >
                <Row className="ingredient">
                    <Col xs={1}>
                        <i className="ingredientIcon"><FontAwesomeIcon icon={this.state.category}/></i>
                    </Col>
                    <Col xs={4}>
                        <b>{this.state.name}</b>
                    </Col>
                    {currentlyEditing ?
                        <>
                            <Col>
                                <Form.Control name="editedQuantity" onChange={this.handleChange}/>
                            </Col>
                            <Col xs={1}>
                                {this.state.measurementName}
                            </Col>
                            <Col xs={1}>
                                <Button size="sm" variant="outline-success" onClick={this.updateIngredient}>
                                    <FontAwesomeIcon icon={faCheck}/>
                                </Button>
                            </Col>
                        </> :
                        <Col>{this.state.quantity} {this.state.measurementName}</Col>
                    }
                    <Col xs={1}>
                        <Dropdown>
                            <DropdownToggle variant="outline-secondary" size="sm">
                            </DropdownToggle>
                            <DropdownMenu>
                                <DropdownItem onClick={() => this.deleteIngredient()}>Delete</DropdownItem>
                                <DropdownItem onClick={() => this.startEditIngredient()}>Edit</DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                    </Col>
                </Row>
            </ListGroup.Item>
        )
    }

}

export default Ingredient;