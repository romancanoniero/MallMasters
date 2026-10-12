# Mall Masters Scanner (iOS)

Native SwiftUI + ARKit/RealityKit LiDAR proof-of-concept.

## What v0.1 does
- Starts and stops AR world tracking on a LiDAR-capable iPhone.
- Enables ARKit scene mesh reconstruction.
- Overlays the scene mesh and feature points.
- Displays a count of ARMeshAnchor objects and tracking status.

## Not implemented yet
Mesh export, persistent scan sessions, multi-floor registration, cloud upload,
server-side SLAM alignment, accounts, permissions and TestFlight signing.

## Cloud compilation
Codemagic recognizes `/codemagic.yaml` in the repository root. The initial
`scanner-ios-check` workflow builds for physical iOS devices but does not sign.
A successful build validates source compilation only; it is not an IPA installable
on an iPhone. A paid Apple Developer membership, App Store Connect record,
Codemagic Apple integration and distribution signing are required for TestFlight.

## Privacy
The camera is used for local AR tracking. No scan or video is uploaded by v0.1.
