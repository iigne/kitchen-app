import React from "react";
import Ingredient from "../Ingredient/Ingredient";

import './UserStock.css';
import AddIngredient from "../Ingredient/AddIngredient";
import {Container, ListGroup} from "react-bootstrap";
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
            console.log(error);
            this.props.showAlert("Failed fetching ingredients", "error");
        })
    }

    handleAddIngredient = (ingredient) => {
        axios.post('/user-ingredient', {
            ingredientId: ingredient.id,
            quantity: ingredient.quantity,
            measurementId: ingredient.measurementId
        }, {
            headers: authHeader()
        }).then(res => {
            const ingredientResult = res.data;
            this.setState(prevState => {
                const ingredients = prevState.ingredients
                const index = ingredients.findIndex(ui => ui.id === ingredientResult.id)
                if(index === -1) {
                    ingredients.push(ingredientResult)
                } else {
                    ingredients.splice(index, 1, ingredientResult)
                }
                return {ingredients: ingredients}
            })
        }).catch(error => {
            this.props.showAlert("Failed adding ingredient", "error");
            console.log(error);
        });
    }

    handleRemoveIngredient = (id) => {
        axios.delete('/user-ingredient', {
            params: {ingredientId: id},
            headers: authHeader()
        }).then(
            res => {
                this.setState(prevState => {
                    const ingredients = prevState.ingredients.filter(u => u.id !== id)
                    return {ingredients: ingredients}
                });
            }
        ).catch(error => {
            this.props.showAlert("Failed removing ingredient", "error");
            console.log(error);
            }
        )
    }

    handleUpdateIngredient = (ingredientData) => {
        const newQuantity = ingredientData.quantity;
        const ingredientId = ingredientData.id;
        const measurement = ingredientData.measurementId;
        axios.patch('/user-ingredient', {
                ingredientId: ingredientId,
                measurementId: measurement,
                quantity: newQuantity
            }, {
                headers: authHeader()
            }
        ).then(res => {
            const ingredient = res.data;
            this.setState(prevState => {
                const ingredients = prevState.ingredients
                const index = ingredients.findIndex(ui => ui.id === ingredient.id)
                ingredients.splice(index, 1, ingredient)
                return {ingredients: ingredients}
            })
        }).catch(error => {
            this.props.showAlert("Failed updating ingredient", "error");
            console.log(error);
        })
    }

    render() {
        let ingredients = this.state.ingredients
        return (
            <div>
                <header className="header">My ingredients</header>
                <Container>
                    {ingredients.length > 0 ?
                        <ListGroup>
                            {ingredients.map((item) =>
                                <Ingredient {...item} key={item.id}
                                            removeIngredientHandler={this.handleRemoveIngredient}
                                            updateIngredientHandler={this.handleUpdateIngredient}/>
                            )}
                        </ListGroup>
                        : <p>Seems like your fridge is empty! Add ingredients below</p>}

                    <header className="subheader">
                        Add ingredients
                    </header>

                    <AddIngredient addIngredientHandler={this.handleAddIngredient} showAlert={this.props.showAlert}/>

                </Container>
            </div>
        );
    }
}

export default UserStock;