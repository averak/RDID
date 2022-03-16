package dev.abelab.rdid.config;

import dev.abelab.rdid.enums.UserStatusEnum;
import dev.abelab.rdid.model.UserModel;
import dev.abelab.rdid.property.ProjectProperty;
import dev.abelab.rdid.repository.UserRepository;
import dev.abelab.rdid.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * データソースの設定
 */
@Profile("prod | local")
@Configuration
@RequiredArgsConstructor
public class DatasourceConfig {

    private final UserRepository userRepository;

    private final ProjectProperty projectProperty;

    private final AuthUtil authUtil;

    @PostConstruct
    public void createDefaultAdmin() {
        // 管理者アカウントが既に存在する
        if (this.userRepository.selectByEmail(this.projectProperty.getAdminAccount().getEmail()).isPresent()) {
            return;
        }

        // 管理者アカウントを作成
        final var adminUser = UserModel.builder() //
            .firstName(this.projectProperty.getAdminAccount().getFirstName()) //
            .lastName(this.projectProperty.getAdminAccount().getLastName()) //
            .email(this.projectProperty.getAdminAccount().getEmail()) //
            .password(this.authUtil.hashingPassword(this.projectProperty.getAdminAccount().getPassword())) //
            .admissionYear(this.projectProperty.getAdminAccount().getAdmissionYear()) //
            .status(UserStatusEnum.ACTIVE) //
            .build();
        this.userRepository.insert(adminUser);
        // TODO: オーナーグループを作成
    }


}
