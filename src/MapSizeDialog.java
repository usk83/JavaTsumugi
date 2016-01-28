import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MapSizeDialog extends JDialog implements ActionListener {
    private JTextField colTextField;
    private JButton okButton;
    private JButton cancelButton;

    private int mapColumns;

    private boolean isOKPressed;

    public MapSizeDialog(JFrame owner) {
        super(owner, "マップ設定", true);

        mapColumns = 0;
        isOKPressed = false;

        colTextField = new JTextField(4);
        okButton = new JButton("OK");
        cancelButton = new JButton("ｷｬﾝｾﾙ");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel colPanel = new JPanel();
        colPanel.add(new JLabel("列数 (25以上)"));
        colPanel.add(colTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 1));
        contentPane.add(colPanel);
        contentPane.add(buttonPanel);

        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            try {
                // MapEditorクラスのインスタンス変数rowとmapColumnsを設定
                mapColumns = Integer.parseInt(colTextField.getText());
                // マップの列数は最低25
                if (mapColumns < 25) {
                    JOptionPane.showMessageDialog(MapSizeDialog.this,
                            "マップの列数は25以上にしてください");
                    return;
                }
            }
            catch (NumberFormatException ex) {
                // テキストボックスに数値以外が入力されたとき
                JOptionPane.showMessageDialog(MapSizeDialog.this,
                        "数値を入力してください");
                colTextField.setText("");
                return;
            }
            isOKPressed = true;
            setVisible(false);
        }
        else if (e.getSource() == cancelButton) {
            isOKPressed = false;
            setVisible(false);
        }
    }

    public boolean isOKPressed() {
        return isOKPressed;
    }

    public int getMapColumns(){
      return mapColumns;
    }
}
