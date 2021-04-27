import React from "react";
import {ListGroup, Row} from "react-bootstrap";
import IngredientBase from "./IngredientBase";

class SearchIngredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            name: props.name,
            quantity: 100,
            measurementId: props.selectedMeasurement.id,
            measurementName:props.selectedMeasurement.name,
            measurements: props.measurements,
            category: props.category
        }
    }

    render() {
        const ingredient = this.state;
        return (
            <ListGroup>
                <ListGroup.Item >
                    <Row className="ingredient">
                        <IngredientBase {...ingredient}
                                        edit={true}
                                        removeIngredientHandler={null}
                                        updateIngredientHandler={this.props.addIngredientHandler}
                        />
                    </Row>
                </ListGroup.Item>
            </ListGroup>
        )
    }

}

export default SearchIngredient;