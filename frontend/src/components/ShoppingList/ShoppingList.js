import React from "react";
import {Container, ListGroup} from "react-bootstrap";
import AddIngredient from "../Ingredient/AddIngredient";
import IconButtonLabel from "../Recipes/IconButtonLabel";
import {faCheck, faTrash} from "@fortawesome/free-solid-svg-icons";
import ShoppingListIngredient from "../Ingredient/ShoppingListIngredient";


class ShoppingList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ingredients: [
                {
                    id: 7,
                    name: "Avocado",
                    quantity: 100,
                    measurementId: 1,
                    measurementName: "g",
                    category: "Vegetable",

                    ticked: false
                }
            ]
        }
    }

    handleAddIngredient = (ingredient) => {
        //TODO add backend
        ingredient.ticked = false;
        let ingredients = [...this.state.ingredients];
        ingredients.push(ingredient);
        this.setState({ingredients: ingredients});
    }

    handleRemoveIngredient = (id) => {
        //TODO add backend
        this.setState(prevState => {
            const ingredients = prevState.ingredients.filter(i => i.id !== id)
            return {ingredients: ingredients}
        });
    }

    handleUpdateIngredient = (ingredientData) => {
        const newQuantity = ingredientData.newQuantity;
        const ingredientId = ingredientData.ingredientId;
        const measurement = ingredientData.measurementId;
    }

    handleCheckboxChange = (id) => {
        this.setState(prevState => {
            const oldIngredients = prevState.ingredients;
            const index = oldIngredients.findIndex(i => i.id === id);
            const ingredient = oldIngredients[index];
            ingredient.ticked = !ingredient.ticked;
            console.log(ingredient);
            oldIngredients.splice(index, 1, ingredient);
            return {ingredients: oldIngredients}
        })
    }

    handleFinishShopping = () => {
        //TODO connect to backend
        this.setState(prevState => {
            console.log(prevState.ingredients)
            const ingredients = prevState.ingredients.filter(i => !i.ticked)
            return {ingredients: ingredients}
        })
    }

    handleClearList = () => {
        //TODO add backend
        this.setState({ingredients: []})
    }


    render() {
        const ingredients = this.state.ingredients;
        return (
            <Container>
                <header className="header">Shopping list</header>
                <Container>
                    <ListGroup>
                        {ingredients.map((item) =>
                            <ShoppingListIngredient {...item} key={item.id}
                                        handleCheckboxChange={this.handleCheckboxChange}
                                        removeIngredientHandler={this.handleRemoveIngredient}
                                        updateIngredientHandler={this.handleUpdateIngredient}/>

                        )}
                    </ListGroup>
                </Container>
                <header className="subheader">Add ingredients</header>
                <AddIngredient addIngredientHandler={this.handleAddIngredient}/>
                <Container>
                    <IconButtonLabel label="Finish shopping" icon={faCheck} variant="success"
                                    handleClick={this.handleFinishShopping}
                    />
                    <IconButtonLabel label="Clear shopping list" icon={faTrash} variant="danger"
                                    handleClick={this.handleClearList}
                    />
                </Container>


            </Container>
        );
    }
} export default ShoppingList;