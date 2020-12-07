import React from "react";
import Ingredient from "./Ingredient";

import './UserStock.css';
import AddIngredient from "./AddIngredient";
import {Container} from "react-bootstrap";

class UserStock extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            ingredients: [
                {name: "Bread", quantity: "1", measurement: "unit", category: "Bread"},
                {name: "Spaghetti", quantity: "500", measurement: "grams", category: "Cupboard"},
                {name: "Tofu", quantity: "500", measurement: "grams", category: "Fridge"},
                {name: "Leek", quantity: "1", measurement: "unit", category: "Vegetable"},
                {name: "Chickpeas", quantity: "4", measurement: "cans", category: "Cupboard"},
            ]
        }
    }

    handleAddIngredient = (ingredient) => {
        let ingredients = [...this.state.ingredients];
        ingredients.push(ingredient)
        this.setState({ingredients: ingredients})
    }

    render() {
        return(
            <div>
                <h2>My ingredients</h2>
                <Container>
                    {this.state.ingredients.map((item) =>
                        <Ingredient {...item}/>
                    )}
                    <hr/>
                    <h5>
                        Add ingredients:

                    </h5>

                    <AddIngredient addIngredientHandler={this.handleAddIngredient}/>

                </Container>
            </div>
        );
    }
}

export default UserStock;