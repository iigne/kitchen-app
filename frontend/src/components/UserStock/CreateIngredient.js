import {Container, Form, FormGroup} from "react-bootstrap";
import React from "react";

class CreateIngredient extends React.Component {
    render() {
        //TODO WIP
        return(
            <Container>
                <FormGroup>
                    <Form.Label>Ingredient name</Form.Label>
                    <Form.Control></Form.Control>
                </FormGroup>

                <FormGroup>
                    <Form.Label>Category</Form.Label>
                    <Form.Control as="select">
                        <option>Vegetable</option>
                    </Form.Control>
                </FormGroup>
                <FormGroup>
                    <Form.Label>Metric unit</Form.Label>
                    <Form.Control as="select">
                        <option>grams</option>
                        <option>millilitres</option>
                    </Form.Control>
                </FormGroup>
                <FormGroup>
                    <Form.Label>Additional measurements</Form.Label>
                    <Form.Text className="text-muted">Add any additional measurements that make sense for this product and corresponding weight of this measurement in selected metric unit, e.g. can (400g), jar (500ml), piece (5g), etc. </Form.Text>

                </FormGroup>
            </Container>
        )
    }
}

export default CreateIngredient;