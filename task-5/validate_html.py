import xml.etree.ElementTree as ET
import sys

def validate_html(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # ElementTree is strict about XML. 
        # Since HTML isn't always valid XML, we'll try to parse just the SVG part if possible, 
        # or the whole thing if it's well-formed.
        ET.fromstring(content)
        print("XML Validation Successful!")
    except ET.ParseError as e:
        print(f"XML Validation Failed: {e}")
        # Print the line where it failed
        lines = content.splitlines()
        if e.position[0] <= len(lines):
            print(f"Line {e.position[0]}: {lines[e.position[0]-1]}")
    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        validate_html(sys.argv[1])
    else:
        print("Please provide a file path.")
