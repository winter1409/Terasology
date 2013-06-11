package frontier.game.modes;


import org.terasology.config.Config;
import org.terasology.game.CoreRegistry;
import org.terasology.game.GameEngine;
import org.terasology.game.modes.StateMainMenu;
import org.terasology.game.paths.PathManager;
import org.terasology.game.types.GameType;
import org.terasology.game.types.SurvivalType;
import org.terasology.logic.manager.GUIManager;
import org.terasology.world.WorldInfo;

import java.io.File;
import java.io.FileFilter;

public class StateQuickStart extends StateMainMenu {
    private GUIManager guiManager;

    @Override
    public void init(GameEngine gameEngine) {

        super.init(gameEngine);

        File worldCatalog = PathManager.getInstance().getWorldPath();

        File[] listFiles = worldCatalog.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    return false;
                }
            }
        });


        try {
            File file = listFiles[0];
            File worldManifest = new File(file, WorldInfo.DEFAULT_FILE_NAME);

            WorldInfo info = WorldInfo.load(worldManifest);

            try {
                CoreRegistry.put(GameType.class, (GameType) Class.forName(info.getGameType().substring(6)).newInstance());
            } catch (Exception e) {
                CoreRegistry.put(GameType.class, new SurvivalType());
            }


            Config config = CoreRegistry.get(Config.class);

            config.getWorldGeneration().setDefaultSeed(info.getSeed());
            config.getWorldGeneration().setWorldTitle(info.getTitle());

            gameEngine.changeState(new StateFrontierLoading(info));
            //   CoreRegistry.get(GameEngine.class).changeState(new StateLoading(info));

        } catch (Exception e) {
            //  guiManager.showMessage("Error", "Failed reading world data object. Sorry1.");
        }
    }


}
