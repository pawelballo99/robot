import javax.media.j3d.*;
import javax.vecmath.Vector3f;
import java.util.Enumeration;

public class GameLoop extends Behavior {

    private WakeupOr oredCriteria;
    private World world;

    public GameLoop(World world){
        this.world = world;
    }

    public void initialize(){
        WakeupCriterion[] theCriteria = new WakeupCriterion[1];
        theCriteria[0] = new WakeupOnElapsedTime(1000/60);
        oredCriteria = new WakeupOr(theCriteria);
        wakeupOn(oredCriteria);
    }

    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
            if (theCriterion instanceof WakeupOnElapsedTime) {
                if(world.objY > 0.15f)
                    world.objY -= 0.01f;
                world.objectTransform.setTranslation(new Vector3f(world.objX, world.objY, world.objZ));
                world.objectTg.setTransform(world.objectTransform);
            }
        }
        wakeupOn(oredCriteria);
    }
}
