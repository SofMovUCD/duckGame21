import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.testing.AssertJSwingTestCaseTemplate;

import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.finder.WindowFinder.findFrame;
public class UiTest extends AssertJSwingTestCaseTemplate {

    /**
     * The main entry point for any tests: the wrapped MainWindow.
     */
    protected FrameFixture frame;
}