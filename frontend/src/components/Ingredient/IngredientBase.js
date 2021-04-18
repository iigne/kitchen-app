import React from "react";
import {faCheck,} from "@fortawesome/free-solid-svg-icons";
import {Button, Col, Dropdown, Form} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownItem from "react-bootstrap/DropdownItem";
import {icons} from "./icons";

class IngredientBase extends React.Component {

    constructor(props) {
        super(props);
        const icon = icons.get(props.category);
        const categoryIcon = icon === undefined ? {icon: null, categoryColour: null} : icon;
        this.state = {
            id: props.id,
            name: props.name,
            quantity: props.quantity,
            measurement: props.measurementId,
            measurementName: props.measurementName,
            measurements: props.measurements,
            category: categoryIcon.icon,
            categoryColour: categoryIcon.color,

            currentlyEditing: false,
            editedQuantity: null
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
        const measurements = [...this.state.measurements];
        const newQuantity = this.state.editedQuantity;
        const ingredientId = this.state.id;
        const measurementId = parseInt(this.state.measurement);

        const index = measurements.findIndex( m => m.id === measurementId)
        const newMeasurementName = measurements[index].name;

        this.props.updateIngredientHandler({
            newQuantity: newQuantity,
            ingredientId: ingredientId,
            measurementId: measurementId
        });

        this.setState({quantity: newQuantity})
        this.setState({currentlyEditing: false})
        this.setState({editedQuantity: null})
        this.setState({measurementName: newMeasurementName});
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value})
    }

    render() {
        const currentlyEditing = this.state.currentlyEditing;
        const icon = this.state.category;
        const currentMeasurement = this.state.measurement;
        const measurements = this.state.measurements;
        const measurementsAvailable = measurements !== undefined;
        const quantity = this.state.quantity;
        return (
            <>
                {
                    icon && <Col xs={1}>
                        <i className="ingredientIcon" style={{color: this.state.categoryColour}}><FontAwesomeIcon
                            icon={this.state.category}/></i>
                    </Col>
                }

                <Col>
                    <b>{this.state.name}</b>
                </Col>
                {currentlyEditing ?
                    <>
                        <Col xs={3}>
                            <Form.Control name="editedQuantity" onChange={this.handleChange} defaultValue={quantity}/>
                        </Col>
                        <Col xs={3}>
                            {measurementsAvailable ?
                                <Form.Control name="measurement" as="select" onChange={this.handleChange} defaultValue={currentMeasurement}>
                                    {measurements.map((item) =>
                                        <option key={item.id} value={item.id}>{item.name}</option>)}
                                </Form.Control>
                                :
                                this.state.measurementName
                            }
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
            </>
        );
    }

}

export default IngredientBase;