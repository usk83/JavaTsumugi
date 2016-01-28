import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.*;

public class SaveDialog extends JDialog implements ActionListener {
    private JTextField fileNameField;
    private JTextField gravityField;
    private JButton saveButton;
    private JButton cancelButton;

    private String fileName;
    private float mapGravity;

    private boolean isSavePressed;

    public SaveDialog(JFrame owner) {
        super(owner, "マップの保存", true);

        fileName = "";
        mapGravity = 1.0f;
        isSavePressed = false;

        fileNameField = new JTextField(10);
        gravityField = new JTextField(2);
        saveButton = new JButton("保存");
        cancelButton = new JButton("ｷｬﾝｾﾙ");
        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel fileNamePanel = new JPanel();
        fileNamePanel.add(new JLabel("マップ名"));
        fileNamePanel.add(fileNameField);

        JPanel gravityPanel = new JPanel();
        gravityPanel.add(new JLabel("重力 (0.2~5.0)"));
        gravityPanel.add(gravityField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 1));
        contentPane.add(fileNamePanel);
        contentPane.add(gravityPanel);
        contentPane.add(buttonPanel);

        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            try {
                fileName = fileNameField.getText();

                URI uri = new URI(getClass().getClassLoader().getResource("res/map").toString());
                File[] files = new File(uri).listFiles();

                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.isFile()) {
                        String fileFullName = file.getName();
                        String existingFileName = "";
                        int point = fileFullName.lastIndexOf(".");
                        if (point != -1) {
                            existingFileName = fileFullName.substring(0, point);
                        }
                        if (fileName.equals(existingFileName)) {
                            JOptionPane.showMessageDialog(SaveDialog.this,
                                    "マップがすでに存在します");
                            return;
                        }
                    }
                }

                mapGravity = Float.parseFloat(gravityField.getText());

                if (mapGravity < 0.2 || mapGravity > 5.0) {
                    JOptionPane.showMessageDialog(SaveDialog.this,
                            "重力が有効な値ではありません");
                    return;
                }
            }
            catch (NumberFormatException nfex) {
                JOptionPane.showMessageDialog(SaveDialog.this,
                        "重力は数値で入力してください");
                gravityField.setText("");
                return;
            }
            catch (URISyntaxException usex) {}

            isSavePressed = true;
            setVisible(false);
        }
        else if (e.getSource() == cancelButton) {
            isSavePressed = false;
            setVisible(false);
        }
    }

    public boolean isSavePressed() {
        return isSavePressed;
    }

    public String getFileName() {
        return fileName;
    }

    public float getMapGravity() {
        return mapGravity;
    }
}
