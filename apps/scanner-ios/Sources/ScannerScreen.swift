import SwiftUI
import ARKit
import RealityKit

struct ScannerScreen: View {
    @State private var isScanning = false
    @State private var status = "Listo para iniciar"
    @State private var meshCount = 0

    var body: some View {
        ZStack(alignment: .bottom) {
            ScannerPreview(isScanning: $isScanning, status: $status, meshCount: $meshCount)
                .ignoresSafeArea()

            VStack(spacing: 12) {
                Text("MALL MASTERS")
                    .font(.headline)
                    .tracking(3)
                Text("Scanner LiDAR · Prototipo 0.1")
                    .font(.subheadline)
                Text(status)
                    .font(.footnote)
                Text("Segmentos de malla: \(meshCount)")
                    .font(.footnote)
                    .monospacedDigit()
                Button(isScanning ? "Detener escaneo" : "Iniciar escaneo") {
                    isScanning.toggle()
                }
                .buttonStyle(.borderedProminent)
                .tint(isScanning ? .red : .cyan)
                Text("Esta versión visualiza la malla ARKit. Aún no exporta ni sube geometría.")
                    .font(.caption2)
                    .multilineTextAlignment(.center)
            }
            .foregroundStyle(.white)
            .padding(20)
            .frame(maxWidth: .infinity)
            .background(.black.opacity(0.83), in: RoundedRectangle(cornerRadius: 22))
            .padding()
        }
        .preferredColorScheme(.dark)
    }
}

struct ScannerPreview: UIViewRepresentable {
    @Binding var isScanning: Bool
    @Binding var status: String
    @Binding var meshCount: Int

    func makeCoordinator() -> Coordinator {
        Coordinator(status: $status, meshCount: $meshCount)
    }

    func makeUIView(context: Context) -> ARView {
        let view = ARView(frame: .zero)
        view.automaticallyConfigureSession = false
        view.debugOptions = [.showSceneUnderstanding, .showFeaturePoints]
        view.session.delegate = context.coordinator
        return view
    }

    func updateUIView(_ view: ARView, context: Context) {
        guard isScanning else {
            view.session.pause()
            return
        }
        guard ARWorldTrackingConfiguration.supportsSceneReconstruction(.mesh) else {
            DispatchQueue.main.async { status = "Este dispositivo no admite reconstrucción de malla ARKit." }
            return
        }
        let config = ARWorldTrackingConfiguration()
        config.sceneReconstruction = .mesh
        config.planeDetection = [.horizontal, .vertical]
        view.session.run(config)
    }

    final class Coordinator: NSObject, ARSessionDelegate {
        @Binding var status: String
        @Binding var meshCount: Int

        init(status: Binding<String>, meshCount: Binding<Int>) {
            _status = status
            _meshCount = meshCount
        }

        func session(_ session: ARSession, didUpdate frame: ARFrame) {
            let count = frame.anchors.compactMap { $0 as? ARMeshAnchor }.count
            DispatchQueue.main.async {
                self.meshCount = count
                switch frame.camera.trackingState {
                case .normal: self.status = "Escaneando: tracking estable"
                case .notAvailable: self.status = "Tracking no disponible"
                case .limited: self.status = "Tracking limitado: mover el iPhone lentamente"
                }
            }
        }

        func session(_ session: ARSession, didFailWithError error: Error) {
            DispatchQueue.main.async {
                self.status = "Error ARKit: \(error.localizedDescription)"
            }
        }
    }
}
