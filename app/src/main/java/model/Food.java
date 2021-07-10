package model;

public class Food {
    private int resId;
    private String foodName, description, ingredients;
    private String imagePath;

    public Food(int resId, String foodName) {
        this.resId = resId;
        this.foodName = foodName;
    }

    public Food(int resId, String foodName, String description, String ingredients) {
        this.resId = resId;
        this.foodName = foodName;
        this.description = description;
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
