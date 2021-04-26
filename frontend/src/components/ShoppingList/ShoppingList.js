import React from "react";
import {Container, ListGroup} from "react-bootstrap";
import AddIngredient from "../Ingredient/AddIngredient";
import IconButtonLabel from "../Recipes/IconButtonLabel";
import {faCheck, faTrash} from "@fortawesome/free-solid-svg-icons";
import ShoppingListIngredient from "../Ingredient/ShoppingListIngredient";
import '../Ingredient/ShoppingIngredient.css';
import {
    clearShoppingList,
    createShoppingItem,
    deleteShoppingItem,
    getShoppingItems,
    importShoppingList,
    tickShoppingItem,
    updateShoppingItem
} from "../../api/Api";


class ShoppingList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ingredients: []
        }
    }

    componentDidMount() {
        getShoppingItems((res) => {
            this.setState({ingredients: res.data})
        }, () => {
            this.props.showAlert("Failed fetching shopping list items", "error");
        })
    }

    handleAddIngredient = (ingredient) => {
        const addedIngredient = {
            ingredientId: ingredient.id,
            quantity: ingredient.quantity,
            measurementId: ingredient.measurementId
        };
        createShoppingItem(addedIngredient, (res) => {
            let ingredients = [...this.state.ingredients];
            ingredients.push(res.data);
            this.setState({ingredients: ingredients});
        }, () => {
            this.props.showAlert("Failed adding shopping list item", "error");
        });
    }

    handleRemoveIngredient = (id) => {
        deleteShoppingItem(id, () => {
                this.setState(prevState => {
                    const ingredients = prevState.ingredients.filter(u => u.id !== id);
                    return {ingredients: ingredients}
                });
            }, () => {
                this.props.showAlert("Failed removing shopping list item", "error");
            }
        );
    }

    handleUpdateIngredient = (ingredientData) => {
        const updatedIngredient = {
            ingredientId: ingredientData.id,
            measurementId: ingredientData.measurementId,
            quantity: ingredientData.quantity
        }
        updateShoppingItem(updatedIngredient, (res) => {
            const ingredient = res.data;
            this.setState(prevState => {
                const ingredients = prevState.ingredients;
                const index = ingredients.findIndex(ui => ui.id === ingredient.id);
                ingredients.splice(index, 1, ingredient);
                return {ingredients: ingredients}
            })
        }, () => {
            this.props.showAlert("Failed updating shopping list item", "error");
        })
    }

    handleCheckboxChange = (ingredient) => {
        const id = ingredient.id;
        tickShoppingItem(id, (res) => {
            ingredient.ticked = res.data.ticked;
            this.setState(prevState => {
                const oldIngredients = prevState.ingredients;
                const index = oldIngredients.findIndex(i => i.id === ingredient.id);
                oldIngredients.splice(index, 1, ingredient);
                return {ingredients: oldIngredients};
            })
        }, () => {
            this.props.showAlert("Failed updating shopping list item status", "error");
        });

    }

    handleFinishShopping = () => {
        importShoppingList(() => {
            this.setState(prevState => {
                const ingredients = prevState.ingredients.filter(i => !i.ticked);
                return {ingredients: ingredients};
            })
            this.props.showAlert("Ticked items have been added to your stock", "success");
        }, () => {
            this.props.showAlert("Failed to finish shopping", "error");
        });
    }

    handleClearList = () => {
        clearShoppingList(() => {
            this.setState({ingredients: []})
        }, () => {
            this.props.showAlert("Failed to clear list", "error");
        });
    }

    render() {
        const ingredients = this.state.ingredients;
        const noIngredientsMessage = ingredients.length === 0 ?
            <p>Shopping list is currently empty. Try adding some items to your shopping list by searching below or
                by clicking "add to shopping list" button on a recipe.
            </p> : null;
        return (
            <Container>
                <header className="header">Shopping list</header>
                <Container>
                    {noIngredientsMessage}
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
                <AddIngredient addIngredientHandler={this.handleAddIngredient} showAlert={this.props.showAlert}/>
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
}

export default ShoppingList;