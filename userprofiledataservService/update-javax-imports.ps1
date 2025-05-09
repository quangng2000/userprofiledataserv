$javaFiles = Get-ChildItem -Path "c:\Users\nguye\OneDrive\Documents\user-management-master" -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    $content = Get-Content -Path $file.FullName -Raw
    
    # Replace javax.validation with jakarta.validation
    $content = $content -replace "import javax\.validation", "import jakarta.validation"
    
    # Replace javax.servlet with jakarta.servlet
    $content = $content -replace "import javax\.servlet", "import jakarta.servlet"
    
    # Replace javax.annotation with jakarta.annotation
    $content = $content -replace "import javax\.annotation", "import jakarta.annotation"
    
    # Write the updated content back to the file
    Set-Content -Path $file.FullName -Value $content
}

Write-Host "Import replacements complete!"
