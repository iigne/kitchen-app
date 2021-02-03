import React from "react";
import Ingredient from "./Ingredient";

import './UserStock.css';
import AddIngredient from "./AddIngredient";
import {Container} from "react-bootstrap";
import authHeader from "../../api/auth-header";
import axios from "axios";

class UserStock extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            ingredients: []
        }
    }

    componentDidMount() {
        axios.get('/user-ingredient', {headers: authHeader()}).then(
            res => {
                this.setState({ingredients: res.data})
            }
        ).catch(error => {
            console.log(error)
        })
    }

    handleAddIngredient = (ingredient) => {
        let ingredients = [...this.state.ingredients];
        ingredients.push(ingredient)
        this.setState({ingredients: ingredients})
    }

    handleRemoveIngredient = (id) => {
        this.setState(prevState => {
            const ingredients = prevState.ingredients.filter(u => u.ingredient.id !== id)
            return {ingredients: ingredients}
        })
    }

    handleUpdateIngredient = (ingredient) => {
        this.setState(prevState => {
            const ingredients = prevState.ingredients
            const index = ingredients.findIndex(ui => ui.ingredient.id === ingredient.id)
            ingredients.splice(index, 0, ingredient)
            return {ingredients: ingredients}
        })
    }

    render() {
        return (
            <div>
                <h2>My ingredients</h2>
                <Container>
                    {this.state.ingredients.map((item) =>
                        <Ingredient {...item} key={item.ingredient.id}
                                    removeIngredientHandler={this.handleRemoveIngredient}
                                    updateIngredientHandler={this.handleUpdateIngredient}/>
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