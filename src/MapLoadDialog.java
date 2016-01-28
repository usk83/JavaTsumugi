import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.swing.*;

public class MapLoadDialog extends JDialog implements ActionListener {
    private JButton okButton;
    private JButton cancelButton;
    private JComboBox<String> mapBox;

    private String loadMapName;

    private boolean isOKPressed;

    public MapLoadDialog(JFrame owner) {
        super(owner, "マップを開く", true);

        loadMapName = "";
        isOKPressed = false;

        okButton = new JButton("OK");
        cancelButton = new JButton("ｷｬﾝｾﾙ");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);


        List<String> mapList = new ArrayList<String>();

        try {
            URI uri = new URI(getClass().getClassLoader().getResource("res/map").toString());
            File[] files = new File(uri).listFiles();

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    String fileFullName = file.getName();
                    String fileName = "";
                    int point = fileFullName.lastIndexOf(".");
                    if (point != -1) {
                        fileName = fileFullName.substring(0, point);
                    }
                    if (file.getPath().endsWith(".dat")) {
                        mapList.add(fileName);
                    }
                }
            }
        } catch (URISyntaxException e) {}

        String[] maps = (String[])mapList.toArray(new String[0]);
        mapBox = new JComboBox<String>(maps);

        JPanel mapPanel = new JPanel();
        mapPanel.add(mapBox);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 1));
        contentPane.add(mapPanel);
        contentPane.add(buttonPanel);

        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
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

    public String getLoadMapName() {
        return (String)mapBox.getSelectedItem();
    }
}
