import React from "react";
import IngredientBase from "./IngredientBase";
import '../Ingredient/Ingredient.css';

import  {Button, ListGroup, Row} from "react-bootstrap";
import {faCheck} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";


class ShoppingListIngredient extends React.Component{

    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            name: props.name,
            quantity: props.quantity,
            measurementId: props.measurementId,
            measurementName: props.measurementName,
            category: props.category,
            ticked: props.ticked
        }
    }

    handleCheckboxClick = () => {
        this.props.handleCheckboxChange(this.state.id);
        this.setState(prevState => {
            return {ticked: !prevState.ticked}
        });
    }

    render() {
        const ingredient = this.state;
        const ticked = ingredient.ticked;
        const textStyle = ticked ? {textDecoration: "line-through"} : {textDecoration: "none"};
        const listItemVariant = ticked ? "secondary" : "";
        return(
            <ListGroup.Item variant={listItemVariant}>

            <div style={textStyle} className="ingredient">
                <Row>
                    <Button variant={ticked ? "rounded-checked" : "rounded"}  onClick={this.handleCheckboxClick}>
                            <FontAwesomeIcon icon={faCheck}/>
                    </Button>

                    <IngredientBase {...ingredient}
                                    removeIngredientHandler={this.props.removeIngredientHandler}
                                    updateIngredientHandler={this.props.updateIngredientHandler}
                    />
                </Row>

            </div>
            </ListGroup.Item>
        )
    }

} export default ShoppingListIngredient;