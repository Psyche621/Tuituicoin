# Setup

## Clone repository

Clone repository to a folder of your choosing

```bash
git clone https://github.com/Psyche621/Tuituicoin.git
```

## Run the setup script

Make sure you have Python 3.7 or higher installed, then run the setup script from the root folder of the project. This will compile the project into a shaded JAR file.

Linux/MacOS:
```bash
python3 setup.py
```

Windows:
```powershell
python setup.py
```

## Adding to PATH

After running `python3 setup.py`, add the project directory to your system PATH to use `tuiranode` from anywhere.

### Linux & macOS

Edit your shell configuration file:

Linux/MacOS (Mojave and older):
```bash
echo 'export PATH="'$(pwd)':$PATH"' >> ~/.bashrc
source ~/.bashrc
```

MacOS (Catalina and newer):
```zsh
echo 'export PATH="'$(pwd)':$PATH"' >> ~/.zshrc
source ~/.zshrc
```

Once the PATH has been set, test with:
```bash
tuiranode -V
```

### Windows

GUI Method:
1. Search for "Edit the system environmental variables" in the Start menu and open it.
2. Click **Environmental Variables**
3. Under "User variables", click **New**
    - **Variable name:** `PATH`
    - **Variable value:** Paste the program path 
4. Click **OK** three times
5. **Close and reopen your terminal**

PowerShell Method:

1. Open PowerShell as Administrator and run:

```powershell
$projectPath = "C:\path\to\program"
[Environment]::SetEnvironmentVariable("PATH", "$env:PATH;$projectPath", "User")
```

2. Restart PowerShell and test:

```powershell
tuiranode.bat --help
```

# Feedback

If you find any bugs, performance ineffiencies or any other improvements I can make, please let me know! I won't be accepting merge requests as this is a learning project. Thank you!