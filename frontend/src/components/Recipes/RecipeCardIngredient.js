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
        const hasEnoughIngredient = this.state.recipeQuantity < this.state.ownedQuantity;
        const itemVariant = hasEnoughIngredient ? "success" : "danger";
        return(
            <ListGroupItem variant={itemVariant}>
                <Row>
                    <Col xs={3}>{this.state.name}</Col>
                    <Col xs={3}>{this.state.recipeQuantity} {this.state.measurement}</Col>
                    {/*TODO implement*/}
                    <Col> (you have 0 {this.state.measurement})</Col>
                </Row>

            </ListGroupItem>
        )
    }
} export default RecipeCardIngredient;