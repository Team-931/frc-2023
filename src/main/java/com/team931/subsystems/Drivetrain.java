// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team931.subsystems;

import com.team931.Constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.AnalogGyro;

/** Represents a swerve drive style drivetrain. */
public class Drivetrain {
    public final FalconSwerveModule frontLeft = new FalconSwerveModule(Constants.flDriveId, Constants.flTurnId,
            Constants.flAbsEncoder, 0);
    public final FalconSwerveModule frontRight = new FalconSwerveModule(Constants.frDriveId, Constants.frTurnId,
            Constants.frAbsEncoder, 0);
    public final FalconSwerveModule backLeft = new FalconSwerveModule(Constants.blDriveId, Constants.blTurnId,
            Constants.blAbsEncoder, 0);
    public final FalconSwerveModule backRight = new FalconSwerveModule(Constants.brDriveId, Constants.brTurnId,
            Constants.brAbsEncoder, 0);

    private final AnalogGyro gyro = new AnalogGyro(0);

    private final SwerveDriveOdometry odometry = new SwerveDriveOdometry(
            Constants.DriveTrain.driveTrainKinematics,
            gyro.getRotation2d(),
            new SwerveModulePosition[] {
                    frontLeft.getPosition(),
                    frontRight.getPosition(),
                    backLeft.getPosition(),
                    backRight.getPosition()
            });

    public Drivetrain() {
        gyro.reset();
    }

    /**
     * Method to drive the robot using joystick info.
     *
     * @param xSpeed        Speed of the robot in the x direction (forward).
     * @param ySpeed        Speed of the robot in the y direction (sideways).
     * @param rot           Angular rate of the robot.
     * @param fieldRelative Whether the provided x and y speeds are relative to the
     *                      field.
     */
    public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
        var swerveModuleStates = Constants.DriveTrain.driveTrainKinematics.toSwerveModuleStates(
                fieldRelative
                        ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, gyro.getRotation2d())
                        : new ChassisSpeeds(xSpeed, ySpeed, rot));
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.DriveTrain.maxDriveVelocity);
        frontLeft.setDesiredState(swerveModuleStates[0]);
        frontRight.setDesiredState(swerveModuleStates[1]);
        backLeft.setDesiredState(swerveModuleStates[2]);
        backRight.setDesiredState(swerveModuleStates[3]);
    }

    public void zeroWheels() {
        frontLeft.setModuleAngle(Rotation2d.fromDegrees(0));
        frontRight.setModuleAngle(Rotation2d.fromDegrees(0));
        backLeft.setModuleAngle(Rotation2d.fromDegrees(0));
        backRight.setModuleAngle(Rotation2d.fromDegrees(0));
    }

    public void setAngle45public() {
        frontLeft.setModuleAngle(Rotation2d.fromDegrees(45));
        frontRight.setModuleAngle(Rotation2d.fromDegrees(45));
        backLeft.setModuleAngle(Rotation2d.fromDegrees(45));
        backRight.setModuleAngle(Rotation2d.fromDegrees(45));
    }

    /** Updates the field relative position of the robot. */
    public void updateOdometry() {
        odometry.update(
                gyro.getRotation2d(),
                new SwerveModulePosition[] {
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition()
                });
    }
}
