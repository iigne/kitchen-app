import {Button, Form, FormGroup} from "react-bootstrap";
import React from "react";

import './Ingredient.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import AddMeasurement from "./AddMeasurement";
import {createIngredient} from "../../api/Api";
import {categories} from "../../constants";

class CreateIngredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: "",
            category: "Vegetable",
            metricUnit: "g",
            measurements: [],
            addingNewMeasurement: false,
            categories: Array.from(categories.keys())
        }
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    handleNewMeasurement = () => {
        this.setState(prevState => {
            const measurements = [...prevState.measurements];
            const newMeasurement = {
                name: "",
                metricQuantity: 100,
                metricUnit: prevState.metricUnit,
                submitted: false
            };
            measurements.push(newMeasurement);
            return ({
                addingNewMeasurement: true,
                measurements: measurements
            });
        });
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
            const measurements = [...prevState.measurements].filter(m => m.name !== name);
            return ({
                measurements: measurements,
                addingNewMeasurement: false
            })
        })
    }

    handleSubmit = () => {
        const measurements = [...this.state.measurements]
        const filteredMeasurements = measurements.filter(m => m.name !== "")
        const ingredient = {
            name: this.state.name,
            metricUnit: this.state.metricUnit,
            category: this.state.category,
            measurements: filteredMeasurements
        }
        createIngredient(ingredient, (res) => {
            this.props.handleFinishCreateIngredient(true, this.state.name, res.status);
            this.resetState();
        }, (error) => {
            this.props.handleFinishCreateIngredient(false, null, error.response.status);
        });
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
        const categories = this.state.categories;
        const measurements = this.state.measurements;
        const creatingMeasurement = this.state.addingNewMeasurement;
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
                        <Form.Control as="select" name="category"
                                      value={this.state.category}
                                      onChange={this.handleChange}>
                            {categories.map((category) =>
                                <option key={category}> {category} </option>)}
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
                        <Form.Text className="text-muted">
                            Optional - enter any additional measurements for this product
                            and weight/volume of this measurement in selected metric unit, e.g. can (400g), jar (500ml),
                            piece (5g), etc.
                        </Form.Text>

                        {measurements.map((item) =>
                            <AddMeasurement {...item} key={item.name}
                                            handleAddMeasurement={this.handleAddMeasurement}
                                            handleRemoveMeasurement={this.handleRemoveMeasurement}
                            />
                        )}

                        {!creatingMeasurement &&
                        <Button onClick={this.handleNewMeasurement} variant="outline-success">
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