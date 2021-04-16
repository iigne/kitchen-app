import React from "react";
import {Container, ListGroup} from "react-bootstrap";
import AddIngredient from "../Ingredient/AddIngredient";
import IconButtonLabel from "../Recipes/IconButtonLabel";
import {faCheck, faTrash} from "@fortawesome/free-solid-svg-icons";
import ShoppingListIngredient from "../Ingredient/ShoppingListIngredient";
import '../Ingredient/Ingredient.css';
import axios from "axios";
import authHeader from "../../api/auth-header";


class ShoppingList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ingredients: []
        }
    }

    componentDidMount() {
        axios.get('/shopping', {headers: authHeader()}).then(
            res => {
                this.setState({ingredients: res.data})
            }
        ).catch(error => {
            this.props.showAlert("Failed fetching shopping list items", "error");
            console.log(error)
        })
    }

    handleAddIngredient = (ingredient) => {
        axios.post('/shopping', {
            ingredientId: ingredient.id,
            quantity: ingredient.quantity,
            measurementId: ingredient.measurementId
        }, {
            headers: authHeader()
        }).then(res => {
            let ingredients = [...this.state.ingredients];
            ingredients.push(res.data)
            this.setState({ingredients: ingredients})
        }).catch(error => {
            this.props.showAlert("Failed adding shopping list item", "error");
            console.log(error)
        });
    }

    handleRemoveIngredient = (id) => {
        axios.delete('/shopping', {
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
                this.props.showAlert("Failed removing shopping list item", "error");
                console.log(error)
            }
        )
    }

    handleUpdateIngredient = (ingredientData) => {
        const newQuantity = ingredientData.newQuantity;
        const ingredientId = ingredientData.ingredientId;
        const measurement = ingredientData.measurementId;

        axios.patch('/shopping', {
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
            this.props.showAlert("Failed updating shopping list item", "error");
            console.log(error)
        })
    }

    handleCheckboxChange = (ingredient) => {
        const id = ingredient.id;
        axios.post('/shopping/tick', {}, {
            params: {ingredientId: id},
            headers: authHeader()
        }).then(res => {
                ingredient.ticked = res.data.ticked;
                this.setState(prevState => {
                    const oldIngredients = prevState.ingredients;
                    const index = oldIngredients.findIndex(i => i.id === ingredient.id);
                    oldIngredients.splice(index, 1, ingredient);
                    return {ingredients: oldIngredients};
                })
            }
        ).catch(error => {
            this.props.showAlert("Failed updating shopping list item status", "error");
            console.log(error);
        })

    }

    handleFinishShopping = () => {
        axios.post('/shopping/clear-and-import', {}, {
            headers: authHeader()
        }).then(res => {
            this.setState(prevState => {
                const ingredients = prevState.ingredients.filter(i => !i.ticked);
                return {ingredients: ingredients};
            })
            this.props.showAlert("Ticked items have been added to your stock", "success");
        }).catch(error => {
            this.props.showAlert("Failed to finish shopping", "error");
            console.log(error);
        })
    }

    handleClearList = () => {
        axios.delete('/shopping/multiple', {
            headers: authHeader()
        }).then(res => {
            this.setState({ingredients: []})
        }).catch(error => {
            this.props.showAlert("Failed to clear list", "error");
            console.log(error);
        })
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
}

export default ShoppingList;