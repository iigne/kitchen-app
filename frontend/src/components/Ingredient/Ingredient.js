import React from "react";
import '../UserStock/UserStock.css';
import {Row, ListGroup} from "react-bootstrap";
import IngredientBase from "./IngredientBase";


class Ingredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            name: props.name,
            quantity: props.quantity,
            measurementId: props.measurementId,
            measurementName:props.measurementName,
            category: props.category
        }
    }

    render() {

        const ingredient = this.state;
        return (
            <ListGroup.Item >
                <Row className="ingredient">
                    <IngredientBase {...ingredient}
                        removeIngredientHandler={this.props.removeIngredientHandler}
                        updateIngredientHandler={this.props.updateIngredientHandler}
                    />
                </Row>
            </ListGroup.Item>
        )
    }

}

export default Ingredient;