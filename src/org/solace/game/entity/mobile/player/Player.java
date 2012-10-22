package org.solace.game.entity.mobile.player;

import org.solace.game.content.skills.SkillHandler;
import org.solace.event.impl.PlayerLoginEvent;
import org.solace.event.impl.PlayerLogoutEvent;
import org.solace.network.RSChannelContext;
import org.solace.network.packet.PacketDispatcher;
import org.solace.task.Task;
import org.solace.game.item.container.impl.Equipment;
import org.solace.game.item.container.impl.Inventory;
import org.solace.game.content.PrivateMessaging;
import org.solace.game.content.music.MusicHandler;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.npc.NPCUpdating;
import org.solace.game.map.Location;

/**
 * 
 * @author Faris
 */
public class Player extends Mobile {
        
        /**
         * Player stored objects
         */
    	private Task walkToAction;
	private RSChannelContext channelContext;
	private PlayerAuthentication authenticator;
	private PacketDispatcher packetDispatcher = new PacketDispatcher(this);
	private PrivateMessaging playerMessaging = new PrivateMessaging(this);
        private MusicHandler musichandler = new MusicHandler(this);
	private PlayerUpdating updating = new PlayerUpdating(this);
	private Equipment equipment = new Equipment(this);
	private Inventory inventory = new Inventory(this);
	private SkillHandler skills = new SkillHandler(this);
	private NPCUpdating npcUpdating = new NPCUpdating(this);
        private SkillHandler skillHandler = new SkillHandler(this);
        private PlayerAdvocate advocate = new PlayerAdvocate(this);
        
        /**
         * Player stored primitives
         */
        private int playerHeadIcon = -1;
        private boolean logoutRequired = false;

	public Player(String username, String password, RSChannelContext channelContext) {
            super(new Location(3222, 3222));
            this.authenticator = new PlayerAuthentication(username, password);
            this.channelContext = channelContext;
            setDefaultSkills();
            setDefaultAppearance();
            getUpdateFlags().flag(UpdateFlag.UPDATE_REQUIRED);
            getUpdateFlags().flag(UpdateFlag.APPEARANCE);
            getMobilityManager().running(true);
	}

	/**
	 * Can be used for content implementations but only when absolutely
	 * necessary, this is NOT PI
	 */
	@Override
	public void update() {
		getMobilityManager().processMovement();
	}
        
        
        /**
         * Sets channel context as parsed context
         * @param channelContext
         * @return updated player
         */
	public Player channelContext(RSChannelContext channelContext) {
		this.channelContext = channelContext;
		return this;
	}
        
        /**
         * Returns the channelContext for the player
         * @return 
         */
	public RSChannelContext channelContext() {
		return channelContext;
	}
        
        /**
         * Gets the associated music handler.
         * 
         * @return 
         */
        public MusicHandler getMusicHandler() {
            return musichandler;
        }
        
        /**
         * Returns the packet dispatcher
         * @return 
         */
	public PacketDispatcher getPacketDispatcher() {
		return packetDispatcher;
	}
        
        /**
         * Schedules a new login event for this player
         */
	public void handleLoginData() {
		new PlayerLoginEvent(this).execute();
	}
	
        /**
         * Schedules a new logout event event for this player
         */
	public void handleLogoutData() {
		new PlayerLogoutEvent(this).execute();
	}

	/**
	 * @return the playerMessaging
	 */
	public PrivateMessaging getPrivateMessaging() {
		return playerMessaging;
	}

	/**
	 * @return the playerCredentials
	 */
	public PlayerAuthentication getAuthentication() {
		return authenticator;
	}

	/**
	 * @return the assistant
	 */
	public PlayerUpdating getUpdater() {
		return updating;
	}
        
        public void setFaceEntity(Mobile entity){
            super.setInteractingEntityIndex(entity.getIndex());
            updating.getMaster().getUpdateFlags().faceEntity();
        }

	/**
	 * @return the playerHeadIcon
	 */
	public int getPlayerHeadIcon() {
		return playerHeadIcon;
	}

	/**
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	/**
	 * Sets the players current walk to task
	 * @param walkToAction
	 * @return
	 */
	public Player walkToAction(Task walkToAction) {
		this.walkToAction = walkToAction;
		return this;
	}

	/**
	 * Returns the inventory instance
	 * @return
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gets the walk to action task.
	 * 
	 * @return the walk to action task
	 */
	public Task walkToAction() {
		return walkToAction;
	}
	
	/**
	 * Returns the NPC updating instance
	 * @return
	 */
	public NPCUpdating getNpcUpdating() {
		return npcUpdating;
	}

	/**
	 * Returns the skills instance
	 * @return
	 */
	public SkillHandler getSkills() {
		return skills;
	}
        
        /**
         * @return the logoutRequired
         */
        public boolean isLogoutRequired() {
            return logoutRequired;
        }

        /**
         * @param logoutRequired the logoutRequired to set
         */
        public void setLogoutRequired(boolean logoutRequired) {
            this.logoutRequired = logoutRequired;
        }        
	
        /**
         * Sets the defaulted skill values and experience levels
         */
	private void setDefaultSkills() {
		for (int i = 0; i < getSkills().getPlayerLevel().length; i++) {
			getSkills().getPlayerLevel()[i] = 1;
			getSkills().getPlayerExp()[i] = 0;
		}
		getSkills().getPlayerLevel()[3] = 10;
		getSkills().getPlayerExp()[3] = 1154;
	}

	private void setDefaultAppearance() {
		/**
		 * Gender
		 */
		getAuthentication().appearanceIndex[0] = 0;

		/**
		 * Clothing
		 */
		getAuthentication().appearanceIndex[1] = 0;
		getAuthentication().appearanceIndex[2] = 18;
		getAuthentication().appearanceIndex[3] = 26;
		getAuthentication().appearanceIndex[4] = 35;
		getAuthentication().appearanceIndex[5] = 36;
		getAuthentication().appearanceIndex[6] = 42;
		getAuthentication().appearanceIndex[7] = 10;
		/**
		 * Colors
		 */
		getAuthentication().appearanceIndex[8] = 7;
		getAuthentication().appearanceIndex[9] = 8;
		getAuthentication().appearanceIndex[10] = 9;
		getAuthentication().appearanceIndex[11] = 5;
		getAuthentication().appearanceIndex[12] = 0;

	}

    /**
     * @return the skillHandler
     */
    public SkillHandler getSkillHandler() {
        return skillHandler;
    }

    /**
     * @return the advocate
     */
    public PlayerAdvocate getAdvocate() {
        return advocate;
    }



}