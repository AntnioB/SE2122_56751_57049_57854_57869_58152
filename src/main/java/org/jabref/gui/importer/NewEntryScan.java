package org.jabref.gui.importer;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.controlsfx.control.PopOver;
import org.jabref.gui.DialogService;
import org.jabref.gui.JabRefFrame;
import org.jabref.gui.LibraryTab;
import org.jabref.gui.StateManager;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.gui.util.TaskExecutor;
import org.jabref.preferences.PreferencesService;
import com.github.sarxos.webcam.*;

import javax.swing.*;

import java.awt.*;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import static org.jabref.gui.actions.ActionHelper.needsDatabase;

public class NewEntryScan extends SimpleCommand {

    private final JabRefFrame jabRefFrame;
    private final DialogService dialogService;
    private final PreferencesService preferences;
    private final TaskExecutor taskExecutor;
    private final PopOver entryFromIdPopOver;
    private final StateManager stateManager;

    public NewEntryScan(JabRefFrame jabRefFrame, DialogService dialogService, PreferencesService preferences, StateManager stateManager, TaskExecutor taskExecutor, PopOver entryFromIdPopOver){
        this.jabRefFrame=jabRefFrame;
        this.dialogService= dialogService;
        this.preferences=preferences;
        this.taskExecutor = taskExecutor;
        this.entryFromIdPopOver = entryFromIdPopOver;
        this.stateManager = stateManager;

        this.executable.bind(needsDatabase(stateManager));
    }

    @Override
    public void execute() {
        new QR();
    }

    public class QR extends JFrame implements Runnable, ThreadFactory {

        private static final long serialVersionUID = 6441489157408381878L;

        private Executor executor = Executors.newSingleThreadExecutor(this);

        private Webcam webcam = null;
        private WebcamPanel panel = null;
        private JTextArea textarea = null;
        private Result result = null;


        public QR() {
            super();

            setLayout(new FlowLayout());
            setTitle("Read QR / Bar Code With Webcam");
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            Dimension size = WebcamResolution.QVGA.getSize();

            webcam = Webcam.getWebcams().get(0);
            webcam.setViewSize(size);

            panel = new WebcamPanel(webcam);
            panel.setPreferredSize(size);
            panel.setFPSDisplayed(true);

            textarea = new JTextArea();
            textarea.setEditable(false);
            textarea.setPreferredSize(size);

            add(panel);
            add(textarea);

            pack();
            setVisible(true);
            toFront();

            executor.execute(this);

            WindowAdapter close = new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    webcam.close();
                    dispose();
                }
            };
            addWindowListener(close);
        }

        @Override
        public void run() {

            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                BufferedImage image = null;

                if (webcam.isOpen()) {

                    if ((image = webcam.getImage()) == null) {
                        continue;
                    }

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        result = new MultiFormatReader().decode(bitmap);
                    } catch (NotFoundException e) {
                        // fall thru, it means there is no QR code in image
                    }
                }

                if (result != null) {
                    String isbn=result.getText();
                    textarea.setText(isbn);
                    GenerateEntryFromIdAction action=new GenerateEntryFromIdAction( jabRefFrame.getCurrentLibraryTab(),  dialogService,  preferences,  taskExecutor,  entryFromIdPopOver,  isbn,  stateManager);
                    action.execute();
                }


            } while (result == null);

            webcam.close();
            dispose();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "example-runner");
            t.setDaemon(true);
            return t;
        }
    }
}
