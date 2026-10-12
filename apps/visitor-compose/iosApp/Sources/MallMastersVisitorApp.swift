import SwiftUI
import MallMastersShared

@main
struct MallMastersVisitorApp: App {
    var body: some Scene {
        WindowGroup {
            ComposeRoot().ignoresSafeArea(.keyboard)
        }
    }
}
struct ComposeRoot: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        ComposeViewControllerKt.MainViewController()
    }
    func updateUIViewController(_ controller: UIViewController, context: Context) {}
}
