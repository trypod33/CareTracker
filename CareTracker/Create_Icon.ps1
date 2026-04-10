$base = "C:\Users\16156\Downloads\CareTracker\CareTracker\app\src\main\res"

$png = [Convert]::FromBase64String("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==")

@("mipmap-mdpi","mipmap-hdpi","mipmap-xhdpi","mipmap-xxhdpi","mipmap-xxxhdpi") | ForEach-Object {
    $dir = "$base\$_"
    New-Item -ItemType Directory -Force -Path $dir | Out-Null
    [IO.File]::WriteAllBytes("$dir\ic_launcher.png", $png)
    [IO.File]::WriteAllBytes("$dir\ic_launcher_round.png", $png)
}

Write-Host "Done."
dir $base