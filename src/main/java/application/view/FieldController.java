//package application.view;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.CheckBox;
//
//import java.awt.*;
//
//public class FieldController {
//    public CheckBox im_rus;
//    public CheckBox what;
//
//    @FXML
//    public void pressedButton() {
//        if(im_rus.isSelected()){
//            System.out.println("RUS");
//        } else if(what.isSelected()){
//            System.out.println("LA FRANCE");
//        } else{
//            System.out.println("what");
//        }
//
//    }
//    @FXML
//    public void is_checked(ActionEvent actionEvent) {
//        CheckBox checkBox = (CheckBox) actionEvent.getSource();
//        if (checkBox == im_rus && im_rus.isSelected()) {
//            what.setSelected(false);
//        } else if (checkBox == what && what.isSelected()) {
//            im_rus.setSelected(false);
//        }
//    }
//}
