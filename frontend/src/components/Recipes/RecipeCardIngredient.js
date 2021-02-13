import {Col, ListGroupItem, Row} from "react-bootstrap";
import React from "react";

class RecipeCardIngredient extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: props.name,
            recipeQuantity: props.recipeQuantity,
            ownedQuantity: props.ownedQuantity,
            measurement: props.measurement
        }
    }

    render() {
        const recipeQuantity = this.state.recipeQuantity;
        const ownedQuantity = this.state.ownedQuantity;

        const hasFullIngredient = recipeQuantity <= ownedQuantity;
        const hasPartialIngredient = ownedQuantity > 0;
        const itemVariant = hasFullIngredient ? "success" : (hasPartialIngredient ? "warning" : "danger");
        return(
            <ListGroupItem variant={itemVariant}>
                <Row>
                    <Col xs={3}>{this.state.name}</Col>
                    <Col xs={3}>{recipeQuantity} {this.state.measurement}</Col>
                    {/*TODO implement*/}
                    <Col> (you have {this.state.ownedQuantity} {this.state.measurement})</Col>
                </Row>

            </ListGroupItem>
        )
    }
} export default RecipeCardIngredient;