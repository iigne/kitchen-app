import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faAppleAlt,
    faBox,
    faBreadSlice,
    faCheck,
    faLeaf,
    faPepperHot,
    faSnowflake
} from "@fortawesome/free-solid-svg-icons";

import '../UserStock/UserStock.css';
import {Button, Col, Dropdown, Form, Row, ListGroup} from "react-bootstrap";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownItem from "react-bootstrap/DropdownItem";


class Ingredient extends React.Component {

    constructor(props) {
        super(props);
        const categoryIcon = this.getIcon(props.category);
        this.state = {
            id: props.id,
            name: props.name,
            quantity: props.quantity,
            measurement: props.measurementId,
            measurementName:props.measurementName,
            category: categoryIcon.icon,
            categoryColour: categoryIcon.color,

            currentlyEditing: false,
            editedQuantity: null
        }
    }

    getIcon(category) {
        //TODO this is probably can be done with a map???
        switch (category) {
            case "Vegetable":
                return {
                    icon: faLeaf,
                    color: "#66B447"
                };
            case "Fruit":
                return {
                    icon: faAppleAlt,
                    color:"#E9692C"
                };
            case "Bread":
                return {
                    icon: faBreadSlice,
                    color:" #EEC07B"
                };
            case "Cupboard":
                return {
                    icon: faBox,
                    color:"#d4bc89"
                };
            case "Fridge":
                return {
                    icon: faSnowflake,
                    color:"#a0cbf8"
                };
            case "Freezer":
                return {
                    icon: faSnowflake,
                    color:"#1530b1"
                };
            case "Spice":
                return {
                    icon: faPepperHot,
                    // color:"#E32227"
                };
            default:
                return  {
                    icon: null,
                }
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
        const icon = this.state.category;

        return (
            <ListGroup.Item >
                <Row className="ingredient">
                    {
                        icon && <Col xs={1}>
                            <i className="ingredientIcon" style={{color: this.state.categoryColour}}><FontAwesomeIcon icon={this.state.category}/></i>
                        </Col>
                    }

                    <Col>
                        <b>{this.state.name}</b>
                    </Col>
                    {currentlyEditing ?
                        <>
                            <Col xs={3}>
                                <Form.Control name="editedQuantity" onChange={this.handleChange}/>
                            </Col>
                            <Col>
                                {this.state.measurementName}
                            </Col>
                            <Col xs={1}>
                                <Button size="sm" variant="outline-success" onClick={this.updateIngredient}>
                                    <FontAwesomeIcon icon={faCheck}/>
                                </Button>
                            </Col>
                        </> :
                        <>
                        <Col>{this.state.quantity} {this.state.measurementName}</Col>
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
                        </>
                    }

                </Row>
            </ListGroup.Item>
        )
    }

}

export default Ingredient;