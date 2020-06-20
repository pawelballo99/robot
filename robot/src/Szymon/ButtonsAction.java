import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.swing.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsAction implements ActionListener {

    private World world;
    private Robot robot;

    ButtonsAction(World world, Robot robot){
        this.world = world;
        this.robot = robot;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton) e.getSource();
        if(bt == world.catchObject){
            if(world.objColl && !world.objHold){
                world.objectGroup.detach();
                world.objectTransform.setTranslation(new Vector3f(0.0f, 0.25f, 0.0f));
                world.objectTg.setTransform(world.objectTransform);
                robot.tgArm[5].addChild(world.objectGroup);
                world.objHold = true;
            }
            else {
                System.out.println("Nie można chwycić");
            }
        }
        if(bt == world.letGoObject){
            if(world.objHold){
                world.objectGroup.detach();
                world.objPos = robot.handPos();
                world.objectTransform.setTranslation(world.objPos);
                world.objectTg.setTransform(world.objectTransform);

                CollisionDetector collisionObject = new CollisionDetector(world.object.getShape(), new BoundingSphere(new Point3d(), 0.15), world, robot);
                BranchGroup tmpCollGroup = new BranchGroup();
                tmpCollGroup.addChild(collisionObject);
                world.world.addChild(tmpCollGroup);

                world.world.addChild(world.objectGroup);
                world.objHold = false;
            }
            else{
                System.out.println("Nie można puścić");
            }
        }
        if(bt == world.startRecording){
            // rozpoczęcie nagrywania
            if(!world.recording && !world.recordingPlay) {
                world.recording = true;
                world.recordingCount = 0;
                System.out.println("zaczeto nagrywac");
            } else if(world.recordingPlay){
                System.out.println("Nie mozna nagrywac bo wlasnie sie odtwarza");
            } else{
                System.out.println("wlasnie sie nagrywa");
            }
        }
        if(bt == world.stopRecording){
            // przestanie nagrywania
            if(world.recording) {
                world.recording = false;
                System.out.println("skonczono nagrywac");
            } else{
                System.out.println("nie nagrywa sie");
            }
        }
        if(bt == world.playRecording){
            // odtworzenie nagrywania
            if(!world.recordingPlay && !world.recording) {
                System.out.println("Odtwarzanie nagrania");
                world.recordingPlay = true;
                world.playingRecordCount = 0;
            } else if(world.recording){
                System.out.println("Nie mozna odtworzyc bo wlasnie sie nagrywa");
            }  else{
                System.out.println("Wlasnie sie odtwarza nagrywanie");
            }
        }
        if(bt == world.stopPlayRecording){
            // zatrzymanie odtwarzania nagrania
            if(world.recordingPlay) {
                System.out.println("Zatrzymanie odtwarzania");
                world.recordingPlay = false;
            } else{
                System.out.println("Nie odtwarza sie teraz");
            }
        }
        if(bt == world.resetCameraView){
            Transform3D przesuniecie_obserwatora = new Transform3D();
            przesuniecie_obserwatora.set(new Vector3f(0.0f,0.5f,5.0f));
            world.getSimpleU().getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        }
    }
}
