import {Col, Image, ListGroup, Modal, Row} from "react-bootstrap";
import React from "react";
import RecipeCardIngredient from "./RecipeCardIngredient";
import {
    faHeart,
    faHeartBroken,
    faInfoCircle,
    faPencilAlt,
    faTrash,
    faUtensils
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import IconButtonLabel from "./IconButtonLabel";

class RecipeView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            userId: props.userId,
            recipeAuthorId: props.authorId,
            title: props.title,
            method: this.formatMethod(props.method),
            ingredients: props.ingredients,
            image: props.imageLink,
            isRecipeLiked: false
        }
    }

    formatMethod(method) {
        return method.split(';');
    }

    handleToggleLikeRecipe = () => {
        //TODO
        this.setState(prevState => {
            return {isRecipeLiked: !prevState.isRecipeLiked}
        }, () => {console.log(this.state)})
    }

    handleMakeRecipe = () => {
        //TODO
        console.log("recipe made")
    }

    handleDeleteRecipe = () => {

    }

    handleEditRecipe = () => {

    }

    render() {
        const ingredients = this.state.ingredients;
        const methodSteps = this.state.method;
        const isCreatedByUser = this.state.recipeAuthorId === this.state.userId;
        //TODO this doesnt work
        let likedIcon = this.state.isRecipeLiked ? faHeartBroken : faHeart;
        let likedLabel = (this.state.isRecipeLiked ? "Unlike" : "Like" ) + " this recipe";
        return (
            <Modal show={this.props.show} onHide={() => this.props.hide()}>
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
                            <RecipeCardIngredient name={item.ingredientName} measurement={item.measurement}
                                                  recipeQuantity={item.recipeQuantity} ownedQuantity={item.ownedQuantity}/>
                        )}
                    </ListGroup>

                    <h2>Method</h2>
                    {methodSteps.map((item) =>
                        <Row>
                            <Col>
                                {item}
                            </Col>
                        </Row>
                    )}


                    <h2>Options</h2>
                    <IconButtonLabel label={likedLabel} variant="outline-danger" icon={likedIcon}
                                     handleClick={this.handleToggleLikeRecipe}/>
                    <IconButtonLabel label="Make this recipe" icon={faUtensils} handleClick={this.handleMakeRecipe}/>
                    <Row>
                        <Col>
                            <small>
                                <FontAwesomeIcon icon={faInfoCircle}/> this removes quantities of ingredients
                                used
                                for this recipe from your inventory
                            </small>

                        </Col>
                    </Row>

                    { isCreatedByUser &&
                        <>
                            <hr/>
                            <IconButtonLabel label="Edit this recipe" icon={faPencilAlt} variant="warning"
                                             handleClick={this.handleMakeRecipe}/>
                            <IconButtonLabel label="Delete this recipe" icon={faTrash} variant="danger"
                                             handleClick={this.handleMakeRecipe}/>
                        </>
                    }

                </Modal.Body>
            </Modal>
        )
    }
}

export default RecipeView;