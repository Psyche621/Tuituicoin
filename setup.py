#!/usr/bin/env python3
"""
Setup script for Tuiranode - works on Linux, macOS, and Windows
Builds the JAR and makes the CLI tool executable
"""

import os
import sys
import subprocess
import platform
import stat
from pathlib import Path

def get_script_dir():
    """Get the directory where this setup script is located"""
    return Path(__file__).parent.resolve()

def check_maven():
    """Check if Maven is installed"""
    system = platform.system()
    try:
        """ Explicitly invoke command interpreter on Windows to ensure .cmd files are found """
        if system == 'Windows':
            subprocess.run(['cmd', '/c', 'mvn.cmd', '--version'], capture_output=True, check=True)
        if system in ['Linux', 'Darwin']:
            subprocess.run(['mvn', '--version'], capture_output=True, check=True)
        return True
    except (subprocess.CalledProcessError, FileNotFoundError):
        return False

def build_jar(script_dir):
    """Build the JAR file using Maven"""
    print("Building JAR file...")
    tuituicoin_dir = script_dir / 'tuituicoin'
    system = platform.system()
    
    try:
        """ Explicitly invoke command interpreter on Windows to ensure .cmd files are found """
        if system == 'Windows':
            result = subprocess.run(
                ['cmd', '/c', 'mvn.cmd', 'clean', 'package'],
                cwd=tuituicoin_dir,
                capture_output=False
            )
        if system in ['Linux', 'Darwin']:
            result = subprocess.run(
                ['mvn', 'clean', 'package'],
                cwd=tuituicoin_dir,
                capture_output=False
            )
        if result.returncode != 0:
            print("Error: Maven build failed!")
            return False
        print("JAR built successfully!")
        return True
    except FileNotFoundError:
        print("Error: Maven not found. Please install Maven.")
        return False

def make_executable(script_dir):
    """Make the shell script executable on Unix-like systems"""
    system = platform.system()
    
    if system in ['Linux', 'Darwin']:  # Darwin is macOS
        script_path = script_dir / 'tuiranode'
        if script_path.exists():
            # Add execute permissions
            st = os.stat(script_path)
            os.chmod(script_path, st.st_mode | stat.S_IXUSR | stat.S_IXGRP | stat.S_IXOTH)
            print(f"Made {script_path.name} executable")
            return True
    return False

def print_instructions(script_dir):
    """Print setup instructions for the user"""
    system = platform.system()
    
    print("\n" + "="*60)
    print("Setup complete!")
    print("="*60)

def verify_installation(script_dir):
    """Verify that the JAR file exists"""
    jar_path = script_dir / 'tuituicoin' / 'target' / 'tuituinode-1.0-SNAPSHOT-shaded.jar'
    if jar_path.exists():
        size_mb = jar_path.stat().st_size / (1024 * 1024)
        print(f"JAR verified: {size_mb:.1f} MB\n")
        return True
    else:
        print(f"Error: JAR not found at {jar_path}")
        return False

def main():
    """Main setup function"""
    print("\nTuiranode Setup Script")
    print("="*60 + "\n")
    
    script_dir = get_script_dir()
    print(f"Project directory: {script_dir}\n")
    
    # Check Maven
    if not check_maven():
        print("Error: Maven is not installed. Please install Maven first.")
        print("   https://maven.apache.org/install.html")
        sys.exit(1)
    
    # Build JAR
    if not build_jar(script_dir):
        sys.exit(1)
    
    # Verify JAR
    if not verify_installation(script_dir):
        sys.exit(1)
    
    # Make executable on Unix-like systems
    make_executable(script_dir)
    
    # Print instructions
    print_instructions(script_dir)

if __name__ == '__main__':
    main()
