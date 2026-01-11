package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import service.AuthService;

public class LoginPanel extends VBox {

    public LoginPanel(Runnable onSuccess) {

        setPadding(new Insets(40));
        setSpacing(15);
        setAlignment(Pos.CENTER);

        Label titre = new Label("🔐 Connexion");
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField login = new TextField();
        login.setPromptText("Login");

        PasswordField mdp = new PasswordField();
        mdp.setPromptText("Mot de passe");

        Label msg = new Label();

        Button btn = new Button("Connexion");
        btn.setOnAction(e -> {
            AuthService auth = new AuthService();
            if (auth.login(login.getText(), mdp.getText())) {
                onSuccess.run();
            } else {
                msg.setText("❌ Identifiants incorrects");
            }
        });

        getChildren().addAll(titre, login, mdp, btn, msg);
    }
}
