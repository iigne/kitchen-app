import React from "react";
import  {Button, Col, Form, FormGroup, FormLabel, InputGroup} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus, faTrash} from "@fortawesome/free-solid-svg-icons";

class AddMeasurement extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: props.name,
            metricQuantity: props.metricQuantity,
            metricUnit: props.metricUnit,
            submitted: props.submitted
        }
    }

    static getDerivedStateFromProps(props,state) {
        if(props.submitted !== state.submitted) {
            return {
                name: props.name,
                metricQuantity: props.metricQuantity,
                metricUnit: props.metricUnit,
                submitted: props.submitted
            }
        }
        return null;
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value})
    }

    render() {
        const submitted = this.state.submitted;
        const measurementName = submitted ? this.state.name : "";
        const measurement = this.state;
        return (
                <Form.Row>
                    <Col xs={1} className="form-button">
                        <Button variant="outline-danger" size="sm"
                                onClick={() => this.props.handleRemoveMeasurement(measurementName)}>
                            <FontAwesomeIcon icon={faTrash}/>
                        </Button>
                    </Col>

                    <Col>
                        <FormGroup>
                            <FormLabel>Measurement name</FormLabel>
                            <Form.Control value={this.state.name} name="name"
                                          onChange={this.handleChange}
                                          readOnly={submitted}/>
                        </FormGroup>
                    </Col>

                    <Col>
                        <FormGroup>
                            <FormLabel>Weight/volume in metric</FormLabel>
                            <InputGroup>
                                <Form.Control value={this.state.metricQuantity} name="metricQuantity"
                                              onChange={this.handleChange}
                                              type="number" readOnly={submitted}/>
                                <InputGroup.Append>
                                    <InputGroup.Text>
                                        {this.state.metricUnit}
                                    </InputGroup.Text>
                                </InputGroup.Append>
                            </InputGroup>
                        </FormGroup>
                    </Col>

                    <Col xs={1} className="form-button">
                        {!submitted &&
                        <Button onClick={() => this.props.handleAddMeasurement(measurement)} variant="outline-success" size="sm">
                            <FontAwesomeIcon icon={faPlus}/>
                        </Button>
                        }
                    </Col>

                </Form.Row>

        );
    }


}

export default AddMeasurement;