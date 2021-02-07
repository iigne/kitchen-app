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
import authHeader from "../../api/auth-header";


class Ingredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: props.ingredient.id,
            name: props.ingredient.name,
            quantity: props.quantities[0].quantity,
            measurement: props.quantities[0],
            category: this.getIcon(props.ingredient.category),

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
        const id = this.state.id
        axios.delete('/user-ingredient', {
            params: {ingredientId: id},
            headers: authHeader()
        }).then(
            res => {
                this.props.removeIngredientHandler(id)
            }
        ).catch(error => {
                console.log(error)
            }
        )
    }

    startEditIngredient = () => {
        this.setState({editedQuantity: this.state.quantity})
        this.setState({currentlyEditing: true})
    }

    updateIngredient = () => {
        const newQuantity = this.state.editedQuantity
        const ingredientId = this.state.id
        const measurement = this.state.measurement.measurementId
        axios.patch('/user-ingredient/quantity', {
                measurementId: measurement,
                quantity: newQuantity
            }, {
                params: {ingredientId: ingredientId},
                headers: authHeader()
            }
        ).then(res => {
            const updatedQuantity = res.data.quantities[0].quantity;
            this.setState({quantity: updatedQuantity})
            this.setState({currentlyEditing: false})
            this.setState({editedQuantity: null})
        }).catch(error => {
            console.log(error)
        })
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
                                {this.state.measurement.measurementName}
                            </Col>
                            <Col xs={1}>
                                <Button size="sm" variant="outline-success" onClick={this.updateIngredient}>
                                    <FontAwesomeIcon icon={faCheck}/>
                                </Button>
                            </Col>
                        </> :
                        <Col>{this.state.quantity} {this.state.measurement.measurementName}</Col>
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