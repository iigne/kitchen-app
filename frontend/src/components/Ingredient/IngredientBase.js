import React from "react";
import {faCheck,} from "@fortawesome/free-solid-svg-icons";
import {Button, Col, Dropdown, Form} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownItem from "react-bootstrap/DropdownItem";
import {categories} from "../../constants";

class IngredientBase extends React.Component {

    constructor(props) {
        super(props);
        const icon = categories.get(props.category);
        const categoryIcon = icon === undefined ? {icon: null, categoryColour: null} : icon;
        const currentlyEditing = props.edit !== undefined;
        const editedQuantity = props.edit !== undefined ? props.quantity : null;
        this.state = {
            id: props.id,
            name: props.name,
            quantity: props.quantity,
            measurement: props.measurementId,
            measurementName: props.measurementName,
            measurements: props.measurements,
            category: props.category,
            categoryIcon: categoryIcon.icon,
            categoryColour: categoryIcon.color,

            currentlyEditing: currentlyEditing,
            editedQuantity: editedQuantity
        }
    }

    deleteIngredient = () => {
        this.props.removeIngredientHandler(this.state.id);
    }

    startEditIngredient = () => {
        this.setState({
            editedQuantity: this.state.quantity,
            currentlyEditing: true
        });
    }

    updateIngredient = () => {
        const newQuantity = this.state.editedQuantity;
        const measurements = [...this.state.measurements];
        const newMeasurementId = parseInt(this.state.measurement);
        const index = measurements.findIndex(m => m.id === newMeasurementId)
        const newMeasurementName = measurements[index].name;
        this.props.updateIngredientHandler({
            id: this.state.id,
            name: this.state.name,
            quantity: newQuantity,
            category: this.state.category,
            measurementId: newMeasurementId,
            measurementName: newMeasurementName,
            measurement: this.state.measurements
        });
        this.setState({
            quantity: newQuantity,
            currentlyEditing: false,
            editedQuantity: null,
            measurementName: newMeasurementName
        });
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value})
    }

    render() {
        const currentlyEditing = this.state.currentlyEditing;

        const icon = this.state.categoryIcon;
        const iconColour = this.state.categoryColour;
        const currentMeasurement = this.state.measurement;
        const measurements = this.state.measurements;
        const measurementsAvailable = measurements !== undefined;
        const quantity = this.state.quantity;
        return (
            <>
                {icon &&
                <Col xs={1}>
                    <i className="ingredientIcon" style={{color: iconColour}}><FontAwesomeIcon
                        icon={icon}/></i>
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
                                <Form.Control name="measurement" as="select" onChange={this.handleChange}
                                              defaultValue={currentMeasurement}>
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
                    </>

                    :

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