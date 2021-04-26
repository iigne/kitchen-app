import React from "react";
import Ingredient from "../Ingredient/Ingredient";

import './UserStock.css';
import AddIngredient from "../Ingredient/AddIngredient";
import {Container, ListGroup} from "react-bootstrap";
import {createUserIngredient, deleteUserIngredient, getUserIngredients, updateUserIngredient} from "../../api/Api";

class UserStock extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            ingredients: []
        }
    }

    componentDidMount() {
        getUserIngredients((res) => {
            this.setState({ingredients: res.data});
        }, () => {
            this.props.showAlert("Failed fetching ingredients", "error");
        });
    }

    handleAddIngredient = (ingredient) => {
        const addedIngredient = {
            ingredientId: ingredient.id,
            measurementId: ingredient.measurementId,
            quantity: ingredient.quantity
        };
        createUserIngredient(addedIngredient, (res) => {
            const ingredientResult = res.data;
            this.setState(prevState => {
                const ingredients = prevState.ingredients;
                const index = ingredients.findIndex(ui => ui.id === ingredientResult.id);
                if (index === -1) {
                    ingredients.push(ingredientResult);
                } else {
                    this.props.showAlert("Ingredient already exists in your stock. Specified quantity has been added", "info");
                }
                return {ingredients: ingredients};
            });
        }, () => {
            this.props.showAlert("Failed adding ingredient", "error");
        });
    }

    handleRemoveIngredient = (id) => {
        deleteUserIngredient(id, () => {
                this.setState(prevState => {
                    const ingredients = prevState.ingredients.filter(u => u.id !== id);
                    return {ingredients: ingredients};
                });
            }, () => {
                this.props.showAlert("Failed removing ingredient", "error");
            }
        )
    }

    handleUpdateIngredient = (ingredientData) => {
        const updatedIngredient = {
            ingredientId: ingredientData.id,
            measurementId: ingredientData.measurementId,
            quantity: ingredientData.quantity
        }
        updateUserIngredient(updatedIngredient, (res) => {
            const ingredient = res.data;
            this.setState(prevState => {
                const ingredients = prevState.ingredients;
                const index = ingredients.findIndex(ui => ui.id === ingredient.id);
                ingredients.splice(index, 1, ingredient);
                return {ingredients: ingredients};
            })
        }, () => {
            this.props.showAlert("Failed updating ingredient", "error");
        });
    }

    render() {
        let ingredients = this.state.ingredients;
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