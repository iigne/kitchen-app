import {Button, Card, CardImg, ListGroup, ListGroupItem} from "react-bootstrap";
import React from "react";

class RecipeCard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            authorId: props.id,
            title: props.title,
            imageLink: props.imageLink,
            method: props.method,
            ingredients: props.recipeIngredients,
            ownedIngredients: []
        }
    }

    render() {
        const altText = "image of " + this.state.title;
        const ingredients = this.state.ingredients;
        const ownedIngredients = this.state.ownedIngredients;
        return(
            <Card>
                <CardImg variant="top" src={this.state.imageLink} alt={altText}/>
                <Card.Body>
                    <Card.Title>{this.state.title}</Card.Title>
                </Card.Body>
                <ListGroup className="list-group-flush">
                    <ListGroupItem>You have {ownedIngredients.length} out of {ingredients.length} ingredients to make this recipe</ListGroupItem>
                </ListGroup>
                <Card.Footer><Button>See full recipe</Button></Card.Footer>
            </Card>
        )
    }
}

export default RecipeCard;