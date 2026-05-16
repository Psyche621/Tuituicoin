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

```bash
# For bash (most common):
echo 'export PATH="'$(pwd)':$PATH"' >> ~/.bashrc
source ~/.bashrc

# For zsh (newer macOS default):
echo 'export PATH="'$(pwd)':$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### Windows

Coming soon

# Feedback

If you find any bugs, performance ineffiencies or any other improvements I can make, please let me know! I won't be accepting merge requests as this is a learning project. Thank you!