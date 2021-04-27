import {Col, Image, ListGroup, Modal, Row} from "react-bootstrap";
import React from "react";
import RecipeCardIngredient from "./RecipeCardIngredient";
import {
    faHeart,
    faHeartBroken,
    faInfoCircle,
    faPencilAlt,
    faShoppingBasket,
    faTrash,
    faUtensils
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import IconButtonLabel from "./IconButtonLabel";
import RecipeForm from "./RecipeForm";
import {createShoppingItems, deleteRecipe, likeRecipe, removeUserIngredients, updateRecipe} from "../../api/Api";

class RecipeView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            userId: props.userId,
            recipeAuthorId: props.authorId,
            title: props.title,
            method: props.method,
            ingredients: props.ingredients,
            image: props.imageLink,
            liked: props.liked,
            inEditRecipe: false
        }
    }

    formatMethod(method) {
        return method.split(';');
    }

    handleToggleLikeRecipe = () => {
        likeRecipe(this.state.id, res => {
            this.setState(res.data);
        }, () => {
            this.props.showAlert("Failed to like/unlike recipe", "error");
        });
    }

    handleDeleteRecipe = () => {
        deleteRecipe(this.state.id, () => {
                this.props.handleViewRecipe(null);
                this.props.handleRemoveRecipe(this.state.id);
                this.props.showAlert("Recipe has been deleted", "info");
            }, (error) => {
                this.props.showAlert("Failed to delete recipe", "error");
            }
        )
    }

    toggleEditRecipeMode = (status) => {
        this.setState({inEditRecipe: status});
    }

    handleSubmitEditedRecipe = (recipe) => {
        updateRecipe(recipe, (res) => {
            const editedRecipe = res.data;
            this.toggleEditRecipeMode(false);
            this.setState({
                title: editedRecipe.title,
                image: editedRecipe.imageLink,
                method: editedRecipe.method,
                ingredients: editedRecipe.ingredients
            })
            this.props.handleUpdateResults(editedRecipe);
            this.props.showAlert("Recipe has been updated", "success");
        }, () => {
            this.props.showAlert("Failed to update recipe", "error");
        });
    }

    handleMakeRecipe = () => {
        const ingredients = [...this.state.ingredients];
        const usedIngredients = ingredients.map(i => ({
            ingredientId: i.ingredientId,
            measurementId: i.measurementId,
            quantity: i.recipeQuantity
        }));
        removeUserIngredients(usedIngredients, () => {
            this.props.showAlert("Recipe made - used ingredients have been removed from your stock", "info");
        }, () => {
            this.props.showAlert("Failed to make recipe", "error");
        });
    }

    handleAddIngredientsToShopping = () => {
        const ingredients = [...this.state.ingredients];
        const formattedIngredients = ingredients.map(i => ({
            ingredientId: i.ingredientId,
            measurementId: i.measurementId,
            quantity: i.recipeQuantity
        }))
        createShoppingItems(formattedIngredients, () => {
            this.props.showAlert("Ingredients have been added to shopping list", "info");
        }, () => {
            this.props.showAlert("Failed to add ingredients to shopping list", "error");
        });
    }

    render() {
        const recipeId = this.state.id;
        const ingredients = this.state.ingredients;
        const ingredientsFormatted = [...ingredients].map(i => ({
            id: i.ingredientId,
            name: i.ingredientName,
            measurementId: i.measurementId,
            measurementName: i.measurement,
            quantity: i.recipeQuantity,
            measurements: i.measurements
        }))
        const methodSteps = this.formatMethod(this.state.method);
        const isCreatedByUser = this.state.recipeAuthorId === this.state.userId;
        const inEditRecipe = this.state.inEditRecipe;
        let liked = this.state.liked;
        let likedIcon = liked ? faHeartBroken : faHeart;
        let likedLabel = (liked ? "Unlike" : "Like") + " this recipe";
        return (
            <>
                {!inEditRecipe ?

                    <Modal size="lg" show={this.props.show} onHide={() => this.props.handleViewRecipe(recipeId)}>
                        <Modal.Header closeButton>
                            <Modal.Title>
                                {this.state.title}
                            </Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <Image src={this.state.image} fluid/>

                            <h2>Ingredients</h2>
                            <ListGroup>
                                {ingredients.map((item) =>
                                    <RecipeCardIngredient key={item.ingredientId}
                                                          name={item.ingredientName} measurement={item.measurement}
                                                          recipeQuantity={item.recipeQuantity}
                                                          ownedQuantity={item.ownedQuantity}/>
                                )}
                            </ListGroup>

                            <h2>Method</h2>
                            {methodSteps.map((item, index) =>
                                <Row key={index}>
                                    <hr/>
                                    <Col>
                                        {item}
                                    </Col>
                                </Row>
                            )}


                            <h2>Options</h2>
                            <IconButtonLabel label="Add ingredients to shopping list" icon={faShoppingBasket}
                                             variant="outline-secondary"
                                             handleClick={this.handleAddIngredientsToShopping}/>
                            <IconButtonLabel label={likedLabel} variant="outline-danger" icon={likedIcon}
                                             handleClick={this.handleToggleLikeRecipe}/>
                            <IconButtonLabel label="Make this recipe" icon={faUtensils}
                                             handleClick={this.handleMakeRecipe}/>
                            <Row>
                                <Col>
                                    <small>
                                        <FontAwesomeIcon icon={faInfoCircle}/> this removes quantities of ingredients
                                        used
                                        for this recipe from your inventory
                                    </small>

                                </Col>
                            </Row>

                            {isCreatedByUser &&
                            <>
                                <hr/>
                                <IconButtonLabel label="Edit this recipe" icon={faPencilAlt} variant="warning"
                                                 handleClick={() => this.toggleEditRecipeMode(true)}/>
                                <IconButtonLabel label="Delete this recipe" icon={faTrash} variant="danger"
                                                 handleClick={this.handleDeleteRecipe}/>
                            </>
                            }
                        </Modal.Body>
                    </Modal>
                    :
                    <RecipeForm id={recipeId} title={this.state.title} imageLink={this.state.image}
                                method={this.state.method}
                                ingredients={ingredientsFormatted}
                                show={this.state.inEditRecipe}
                                handleCancel={this.toggleEditRecipeMode}
                                handleSubmit={this.handleSubmitEditedRecipe}

                    />

                }
            </>
        )
    }
}

export default RecipeView;