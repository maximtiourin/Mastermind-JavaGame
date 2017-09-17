package com.fizzikgames.mastermind.entity;

import org.newdawn.slick.GameContainer;

import com.fizzikgames.mastermind.Controls;
import com.fizzikgames.mastermind.world.World;

public class PlayerCharacter extends Character {	
	public PlayerCharacter(World world, String spritesheet, double width, double height, double moves) {
		super(world, spritesheet, width, height, moves, RenderableGameObject.RENDER_PLAYERCHARACTER, ControlledEntity.PLAYER_FRIENDLY);
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		
		if ((world.getCurrentPlayerInputFocus() == this) && (!world.isPaused())) {
			//Do moving
			double oldx = this.x;
			double oldy = this.y;
			double dx = 0;
			double dy = 0;
			
			if (gc.getInput().isKeyDown(Controls.move_left)) {
				if (gc.getInput().isKeyDown(Controls.move_up)) {
					dx = -1;
					dy = -1;
				}
				else if (gc.getInput().isKeyDown(Controls.move_down)) {
					dx = -1;
					dy = 1;
				}
				else {
					dx = -1;
					dy = 0;
				}
			}
			else if (gc.getInput().isKeyDown(Controls.move_right)) {
				if (gc.getInput().isKeyDown(Controls.move_up)) {
					dx = 1;
					dy = -1;
				}
				else if (gc.getInput().isKeyDown(Controls.move_down)) {
					dx = 1;
					dy = 1;
				}
				else {
					dx = 1;
					dy = 0;
				}
			}
			else if (gc.getInput().isKeyDown(Controls.move_up)) {
				dx = 0;
				dy = -1;
			}
			else if (gc.getInput().isKeyDown(Controls.move_down)) {
				dx = 0;
				dy = 1;
			}
			
			/* ***********************
			 * test xbox controller 
			 * *********************** */
			/*final int controller = 0;
			if (gc.getInput().isControllerLeft(controller)) {
				if (gc.getInput().isControllerUp(controller)) {
					dx = -1;
					dy = -1;
				}
				else if (gc.getInput().isControllerDown(controller)) {
					dx = -1;
					dy = 1;
				}
				else {
					dx = -1;
					dy = 0;
				}
			}
			else if (gc.getInput().isControllerRight(controller)) {
				if (gc.getInput().isControllerUp(controller)) {
					dx = 1;
					dy = -1;
				}
				else if (gc.getInput().isControllerDown(controller)) {
					dx = 1;
					dy = 1;
				}
				else {
					dx = 1;
					dy = 0;
				}
			}
			else if (gc.getInput().isControllerUp(controller)) {
				dx = 0;
				dy = -1;
			}
			else if (gc.getInput().isControllerDown(controller)) {
				dx = 0;
				dy = 1;
			}
			
			if (gc.getInput().isControlPressed(0, controller)) System.out.println("Button 'LEFT' Pressed");
			if (gc.getInput().isControlPressed(1, controller)) System.out.println("Button 'RIGHT' Pressed");
			if (gc.getInput().isControlPressed(2, controller)) System.out.println("Button 'UP' Pressed");
			if (gc.getInput().isControlPressed(3, controller)) System.out.println("Button 'DOWN' Pressed");
			if (gc.getInput().isControlPressed(4, controller)) System.out.println("Button 'A' Pressed");
			if (gc.getInput().isControlPressed(5, controller)) System.out.println("Button 'B' Pressed");
			if (gc.getInput().isControlPressed(6, controller)) System.out.println("Button 'X' Pressed");
			if (gc.getInput().isControlPressed(7, controller)) System.out.println("Button 'Y' Pressed");
			if (gc.getInput().isControlPressed(8, controller)) System.out.println("Button 'LB' Pressed");
			if (gc.getInput().isControlPressed(9, controller)) System.out.println("Button 'RB' Pressed");
			if (gc.getInput().isControlPressed(10, controller)) System.out.println("Button 'SELECT' Pressed");
			if (gc.getInput().isControlPressed(11, controller)) System.out.println("Button 'START' Pressed");
			if (gc.getInput().isControlPressed(12, controller)) System.out.println("Button 'LS' Pressed");
			if (gc.getInput().isControlPressed(13, controller)) System.out.println("Button 'RS' Pressed");
			*/
			/* *****************************
			 * *********END XBOX test*******
			 * ***************************** */
			
			doMovesImpede(dx, dy);
			
			if ((oldx == this.x) && (oldy == this.y)) {
				//If no positional change, set facing angle to players control pressed direction
				updateRotation(oldx, oldy, oldx + dx, oldy + dy);
			}
			else {
				updateRotation(oldx, oldy, this.x, this.y); //Update the facing angle based on movement
			}
			
			//Set animation if control keys are down and position has changed
			if ((gc.getInput().isKeyDown(Controls.move_up)
					|| gc.getInput().isKeyDown(Controls.move_down)
					|| gc.getInput().isKeyDown(Controls.move_left)
					|| gc.getInput().isKeyDown(Controls.move_right)) && !((oldx == this.x) && (oldy == this.y))) {
				sprite.current("walk_normal");
			}
			else {
				sprite.current("stand_still");
			}
		}
		else {
			sprite.current("stand_still");
		}
		
		sprite.step(delta);
	}
}
