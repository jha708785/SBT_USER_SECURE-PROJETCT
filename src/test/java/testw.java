import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class testw {
    public static void main(String[] args) throws AWTException, InterruptedException {
        Scanner s1=new Scanner(System.in);
        System.out.println("ENTER THE MESSAGE");
        String msg=s1.nextLine();
        System.out.println("HOW MUCH TIMES SEND MESSAGE..");
        int size=s1.nextInt();

        StringSelection selection=new StringSelection(msg);
        Clipboard clipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection,null);

            Thread.sleep(100);




        Robot robot=new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);






    }
}
