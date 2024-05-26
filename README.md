# Imaginator (Image Editor)

Features:
- Brightness
- Contrast
- Temperature
- Hue
- Full Material You
- Android 12+

This project uses only Java code (slow af). **don't expect it to be fast**

I pretend to update it for my personal uses because i feel that android has a hole in image
editing apps, the free ones are old and/or missing features.

Image selection UX feedback is missing btw


### Testing CI workflow locally using [act](https://github.com/nektos/act)

Make sure you have these ENVs setted:
- APK_KEYSTORE (base64 encoded keystore: `base64 < keystore.jks | tr -d '\n'`)
- APK_KEYSTORE_PASSWORD
- APK_ALIAS
- APK_ALIAS_PASSWORD

Then run:

```bash
act \
  -s APK_KEYSTORE \
  -s APK_KEYSTORE_PASSWORD \
  -s APK_ALIAS \
  -s APK_ALIAS_PASSWORD \
  --env RUNNER_TEMP=/root/work/_temp \
  -s GITHUB_TOKEN=(gh auth token)
  -e tag.json
```
