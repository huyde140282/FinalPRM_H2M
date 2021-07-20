package model;

public class Food {
    private int resId;
    private String foodName, description, ingredients, imagePath;
    private int calories, carb, fat;
    public  Food()
    {}
    public Food(String foodName,String description) {
        this.description=description;
        this.foodName = foodName;
    }

    public Food(int resId, String foodName, String description, String ingredients, String imagePath, int calories, int carb, int fat) {
        this.resId = resId;
        this.foodName = foodName;
        this.description = description;
        this.ingredients = ingredients;
        this.imagePath = imagePath;
        this.calories = calories;
        this.carb = carb;
        this.fat = fat;
    }

    public Food(int resId, String foodName, String description, String ingredients, int calories, int carb, int fat) {
        this.resId = resId;
        this.foodName = foodName;
        this.description = description;
        this.ingredients = ingredients;
        this.calories = calories;
        this.carb = carb;
        this.fat = fat;
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCarb() {
        return carb;
    }

    public void setCarb(int carb) {
        this.carb = carb;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }
}
