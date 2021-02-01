import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faAppleAlt, faBox,
    faBreadSlice, faEllipsisH,
    faLeaf,
    faPepperHot,
    faQuestionCircle,
    faSnowflake
} from "@fortawesome/free-solid-svg-icons";

import './UserStock.css';
import Button from "react-bootstrap/Button";
import  {Row, Col, Container} from "react-bootstrap";


class Ingredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: props.ingredient.name,
            quantity: props.quantities[0].quantity,
            measurement: props.quantities[0].measurementName,
            category: this.getIcon(props.ingredient.category)
        }
    }

    getIcon(category) {
        switch (category) {
            case "Vegetable":
                return faLeaf;
            case "Fruit":
                return faAppleAlt;
            case "Bread":
                return faBreadSlice;
            case "Cupboard":
                return faBox;
            case "Fridge":
                return faSnowflake;
            case "Freezer":
                return faSnowflake;
            case "Spice":
                return faPepperHot;
            default:
                return faQuestionCircle;
        }
    }

    render() {
        return(
            <Container className="ingredient">
                <hr/>
                <Row>
                    <Col xs={1}>
                        <i className="ingredientIcon"><FontAwesomeIcon icon={this.state.category}/></i>
                    </Col>
                    <Col>
                        <b>{this.state.name}</b>
                    </Col>
                    <Col>
                        {this.state.quantity} {this.state.measurement}
                    </Col>
                    <Col xs={1} className="float-right">
                        <Button variant="outline-secondary">
                            <FontAwesomeIcon icon={faEllipsisH}/>
                        </Button>
                    </Col>

                </Row>

            </Container>
        )
    }

}

export default Ingredient;