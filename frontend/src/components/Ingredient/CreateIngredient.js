import {Button, Form, FormGroup} from "react-bootstrap";
import React from "react";

import '../UserStock/UserStock.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import AddMeasurement from "./AddMeasurement";
import axios from "axios";

import authHeader from "../../api/auth-header";

class CreateIngredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: "",
            category: "Vegetable",
            metricUnit: "g",
            measurements: [],
            addingNewMeasurement: false,
        }
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value})
    }

    handleNewMeasurement = () => {
        this.setState(prevState => {
            const measurements = [...prevState.measurements]
            measurements.push({
                name: "",
                metricQuantity: 100,
                metricUnit: prevState.metricUnit,
                submitted: false
            })
            return ({
                addingNewMeasurement: true,
                measurements: measurements
            })
        })
    }

    handleAddMeasurement = (measurement) => {
        this.setState(prevState => {
            measurement.submitted = true;
            const measurements = [...prevState.measurements];
            const index = measurements.findIndex(m => m.name === "");
            measurements.splice(index, 1, measurement);
            return ({
                addingNewMeasurement: false,
                measurements: measurements
            })
        })
    }

    handleRemoveMeasurement = (name) => {
        this.setState(prevState => {
            const measurements = [...prevState.measurements].filter(m => m.name !== name)
            return ({
                measurements: measurements,
                addingNewMeasurement: false
            })
        })
    }

    handleSubmit = () => {
        const measurements = [...this.state.measurements]
        const filteredMeasurements = measurements.filter(m => m.name !== "")
        const body = {
            name: this.state.name,
            metricUnit: this.state.metricUnit,
            category: this.state.category,
            measurements: filteredMeasurements
        }
        axios.post('/ingredient', body, {
            headers: authHeader()
        }).then(res => {
            this.props.handleFinishCreateIngredient(true, this.state.name, res.status);
            this.resetState();
        }).catch(error => {
            console.log(error);
            this.props.handleFinishCreateIngredient(false, null, error.response.status);
        })
    }

    resetState = () => {
        this.setState({
            name: "",
            category: "Vegetable",
            metricUnit: "g",
            measurements: [],
            addingNewMeasurement: false,
        })
    }

    handleCancel = () => {
        this.resetState();
        this.props.cancelCreate();
    }

    render() {
        let measurements = this.state.measurements
        let creatingMeasurement = this.state.addingNewMeasurement
        if (!this.props.show) {
            return null
        } else {
            return (
                <Form>
                    <header className="subheader">Create ingredient</header>
                    <FormGroup>
                        <Form.Label>Ingredient name</Form.Label>
                        <Form.Control value={this.state.name} name="name"
                                      onChange={this.handleChange}/>
                    </FormGroup>

                    <FormGroup>
                        <Form.Label>Category</Form.Label>
                        {/*TODO these categories could be moved out to be constants*/}
                        <Form.Control as="select" name="category"
                                      value={this.state.category}
                                      onChange={this.handleChange}>
                            <option>Vegetable</option>
                            <option>Fruit</option>
                            <option>Bread</option>
                            <option>Cupboard</option>
                            <option>Fridge</option>
                            <option>Freezer</option>
                            <option>Spice</option>
                            <option>Other</option>
                        </Form.Control>
                    </FormGroup>
                    <FormGroup>
                        <Form.Label>Metric unit</Form.Label>
                        <Form.Control as="select" name="metricUnit"
                                      onChange={this.handleChange}>
                            <option value="g">grams</option>
                            <option value="ml">millilitres</option>
                        </Form.Control>
                    </FormGroup>


                    <FormGroup>
                        <Form.Label>Additional measurements</Form.Label>
                        <Form.Text className="text-muted">Optional - enter any additional measurements for this product
                            and
                            weight/volume of this measurement in selected metric unit, e.g. can (400g), jar (500ml), piece
                            (5g),
                            etc. </Form.Text>
                        {measurements.map((item) =>
                            <AddMeasurement {...item} key={item.name}
                                            handleAddMeasurement={this.handleAddMeasurement}
                                            handleRemoveMeasurement={this.handleRemoveMeasurement}
                            />
                        )}
                        {!creatingMeasurement &&

                        <Button onClick={this.handleNewMeasurement}
                                variant="outline-success">
                            <FontAwesomeIcon icon={faPlus}/>
                        </Button>
                        }
                    </FormGroup>

                    <FormGroup>
                        <Button onClick={this.handleSubmit}>submit</Button>
                        <Button onClick={this.handleCancel} variant="secondary">cancel</Button>
                    </FormGroup>
                </Form>

            )
        }
    }
}

export default CreateIngredient;