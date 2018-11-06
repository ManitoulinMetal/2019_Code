/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6865.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	
	private DifferentialDrive Move = new DifferentialDrive(new Spark(0), new Spark(1));
	private Joystick xBox = new Joystick(0);
	private Joystick bigJ = new Joystick(1);
	
	private Spark belt = new Spark(6);
	private Spark gate = new Spark(5);
	
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	private String GameData = "EEE";
	
	public Timer time = new Timer();
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Left", "1");
		m_chooser.addObject("Center", "2");
		m_chooser.addObject("Right", "3");
		SmartDashboard.putData("Auto choices", m_chooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		
		m_autoSelected = m_chooser.getSelected();
		 //autoSelected = SmartDashboard.getString("Auto Selector",
		 //defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		GameData = DriverStation.getInstance().getGameSpecificMessage();
		
		time.start();
		auto = true;
		
	}

	/**
	 * This function is called periodically during autonomous.
	 */	
	
	public boolean step = true;
	private boolean auto = true;
	
	
	public void autonomousPeriodic() {
			
		if(auto == true) {
		
			//ROBOT LEFT
			
			if(m_autoSelected.equals("1")) {
				//Switch Left
				if(GameData.startsWith("L")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
					
					if(time.get() > 3.5) {
						Move.arcadeDrive(0, 0);
						belt.set(0.4);
					}
				}
				//Switch Right
				if(GameData.startsWith("R")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
				}
							
			}
			
			//ROBOT MIDDLE AKA CENTER
			
			if(m_autoSelected.equals("2")) {
				//Switch Left
				if(GameData.startsWith("L")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
				}
				//Switch Right
				if(GameData.startsWith("R")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
				}
							
			}
			
			//ROBOT RIGHT
			
			if(m_autoSelected.equals("3")) {
				//Switch Right
				if(GameData.startsWith("R")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
					
					if(time.get() > 3.5) {
						Move.arcadeDrive(0, 0);
						belt.set(0.4);
					}
				}
				//Switch Left
				if(GameData.startsWith("L")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
				}
			}					
		}
	} 				
			
	public void teleopInit() {
		
		//Sets the robot to not move in case still moving from auto
		Move.arcadeDrive(0, 0);
	}
	
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		double beltMove = xBox.getY();
		double xAxis = bigJ.getX();
		double yAxis = bigJ.getY();
		
		// To move the belt and set a dead zone
		if(Math.abs(beltMove) > 0.06) {
			belt.set(beltMove);
		} else {
			belt.set(0);
		}
		
		//To move the arm at a fixed speed(can be more simple)
		if(xBox.getRawButton(3) == true) {
			gate.set(0.8);
		}
		else if(xBox.getRawButton(2) == true) {
			gate.set(-0.8);
		}
		else {
			gate.set(0.0);
		}
		
		//To move the robot, dead zone of 0.05
		if(Math.abs(xAxis) > 0.05 || Math.abs(yAxis) > 0.05) {
			Move.arcadeDrive(-yAxis, xAxis);
		}
		else {
			Move.arcadeDrive(0, 0);
		}
		
		//Adds joystick values to the Dash
		SmartDashboard.putNumber("yAxis", yAxis);
		SmartDashboard.putNumber("XAxis", xAxis);
	}
	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
